(ns planted.db.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [planted.db.util :refer :all]
            [planted.db.schema :as schema])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraphFactory OrientGraph]))

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

(defrecord Database [url admin-user admin-pwd db]
  component/Lifecycle

  (start [this]
    (log/info "Connecting to Orient DB:" url)
    (reset-logging-bridge)
    (if db
      this
      (let [db (connect url admin-user admin-pwd)]
        (schema/init-schema! db schema/planted-schema)
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
