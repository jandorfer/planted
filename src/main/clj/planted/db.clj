(ns planted.db
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraphFactory]))

(defn _get-conn-factory
  [url admin-user admin-pwd]
  (if (nil? admin-user)
    (OrientGraphFactory. url)
    (OrientGraphFactory. url admin-user admin-pwd)))

(defn connect
  "Connects to the database using OrientGraphFactory. If admin user name is nil, uses default admin credentials."
  [url admin-user admin-pwd]
  (let [conn-factory (_get-conn-factory url admin-user admin-pwd)]
    (.setupPool 1 10)
    conn-factory))

(defrecord Database [url admin-user admin-pwd connection]
  component/Lifecycle

  (start [component]
    (log/info "Connecting to Orient DB:" url)
    (if connection
      component
      (let [conn (connect url admin-user admin-pwd)]
        (assoc component :db conn))))

  (stop [component]
    (log/info "Disconnecting from database")
    (if (not connection)
      component
      (do (.close (:db component))
          (assoc component :db nil)))))

(defn new-database [url admin-user admin-pwd]
  (map->Database {:url url :admin-user admin-user :admin-pwd admin-pwd}))
