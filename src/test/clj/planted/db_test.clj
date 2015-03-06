(ns planted.db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [planted.db.core :refer [create search]]
            [planted.system :refer [planted]]))

(deftest test-app
  (let [system (component/start (planted config/env))
        db (get-in system [:db :db])]

    (testing "Create and use Plant type"
      (create db "Plant" {:title "Plant1" :living true})
      (create db "Plant" {:title "Plant2" :living true})
      (create db "Plant" {:title "Plant3" :living false})
      (let [results-living (search db "living" true)
            results-dead (search db "living" false)]
        (is (= (count results-living) 2))
        (is (= (count results-dead) 1))
        (is (= (.getProperty (first results-dead) "title") "Plant3"))))

    (component/stop system)))
