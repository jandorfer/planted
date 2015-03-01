(ns planted.system
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [planted.db :refer [new-database]]
            [planted.webserver :refer [new-webserver]]))

(defn planted [config-options]
  (let [db-url (get-in config-options [:db-url] "plocal:D:/temp/orientdb/dev")
        db-admin-user (get-in config-options [:db-admin-user] nil)
        db-admin-pwd (get-in config-options [:db-admin-pwd] nil)
        bind (Integer. (get-in config-options [:bind] 8080))]
    (log/info "Starting up Planted system...")
    (->
      (component/system-map
        :db (new-database db-url db-admin-user db-admin-pwd)
        :ws (new-webserver bind))
      (component/system-using
        {:ws {:database :db}}))))

(defn -main [& args]
  (let [system (component/start (planted config/env))]
    (.addShutdownHook (Runtime/getRuntime)
      (Thread. #(do
        (log/debug "Planted system began clean shutdown...")
        (component/stop system)
        (log/info "Planted system shutdown complete. Bye bye!"))))))