(ns planted.db
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraphFactory]))

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
    (.setupPool conn-factory 1 10)
    conn-factory))

(defrecord Database [url admin-user admin-pwd connection]
  component/Lifecycle

  (start [this]
    (log/info "Connecting to Orient DB:" url)
    (reset-logging)
    (if connection
      this
      (let [conn (connect url admin-user admin-pwd)]
        (assoc this :connection conn))))

  (stop [this]
    (log/info "Disconnecting from database")
    (if (not connection)
      this
      (do (try (.close connection)
            (catch Throwable t
              (log/warn t "Error when closing database")))
          (assoc this :connection nil)))))

(defn new-database [url admin-user admin-pwd]
  (map->Database {:url url :admin-user admin-user :admin-pwd admin-pwd}))
