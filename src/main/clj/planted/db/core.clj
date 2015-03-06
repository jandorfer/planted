(ns planted.db.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraph]
           [com.tinkerpop.blueprints.impls.orient OrientGraphFactory]))

(defn- reset-logging
  "OrientDB uses java.util.logging, which we need to redirect to slf4j.
  This method ensures the logging is properly reset."
  []
  (org.slf4j.bridge.SLF4JBridgeHandler/removeHandlersForRootLogger)
  (org.slf4j.bridge.SLF4JBridgeHandler/install))

(defn- get-conn-factory
  "Gets a connection factory for the OrientDB database (graph mode).
  Using the connection factory is the preferred method for connection management."
  [url admin-user admin-pwd]
  (if (nil? admin-user)
    (OrientGraphFactory. url)
    (OrientGraphFactory. url admin-user admin-pwd)))

(defn- connect
  "Connects to the database using OrientGraphFactory.
  If admin user name is nil, uses default admin credentials."
  [url admin-user admin-pwd]
  (let [conn-factory (get-conn-factory url admin-user admin-pwd)]
    ;; Before setting up pool, directly connect to ensure DB exists
    (.shutdown (.getNoTx conn-factory))

    ;; Setup pool of connections for the DB
    (.setupPool conn-factory 1 10)

    conn-factory))

(defmacro deftx
  "Writes a function with the given name which takes as its first argument an
  instance of the OrientGraph to work with. Remaining arguments will be passed
  through. The remaining actions (parameters 3+ to this macro) will comprise
  the body of the function to execute against the graph, and said execution
  will take place within the context of a transaction."
  [name args & action]
  (let [graph-arg (first args)
        new-args (rest args)]
    `(defn ~name
       ~(vec (cons 'db-conn-factory new-args))
       (let [~graph-arg (.getTx ~'db-conn-factory)]
         (try
           (let [result# (do ~@action)]
             (.commit ~graph-arg)
             result#)
           (catch Throwable t#
             (.rollback ~graph-arg)
             (throw t#))
           (finally
             (.shutdown ~graph-arg)))))))

(defrecord Database [url admin-user admin-pwd db]
  component/Lifecycle

  (start [this]
    (log/info "Connecting to Orient DB:" url)
    (reset-logging)
    (if db
      this
      (let [db (connect url admin-user admin-pwd)]
        (assoc this :db db))))

  (stop [this]
    (log/info "Disconnecting from database")
    (if (not db)
      this
      (do (try (.close db)
            (catch Throwable t
              (log/warn t "Error when closing database")))
          (assoc this :db nil)))))

(defn new-database [url admin-user admin-pwd]
  (map->Database {:url url :admin-user admin-user :admin-pwd admin-pwd}))
