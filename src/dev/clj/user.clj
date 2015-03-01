(ns user
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :refer [refresh]]
            [planted.system :refer [planted]]
            [environ.core :as config]))

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (planted config/env))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (start))

(defn full-reset []
  (stop)
  (refresh :after 'user/go))