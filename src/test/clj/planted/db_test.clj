(ns planted.db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [planted.db.schema :as dbs]
            [planted.system :refer [planted]]))

(deftest test-app
  (let [system (component/start (planted config/env))
        db (get-in system [:db :db])]

    (testing "Create and use Plant type"
      (dbs/create-plant-class db)
      (dbs/create-plant db "Plant1" true)
      (dbs/create-plant db "Plant2" true)
      (dbs/create-plant db "Plant3" false)
      (let [results-living (dbs/get-plants db "living" true)
            results-dead (dbs/get-plants db "living" false)]
        (is (= (count results-living) 2))
        (is (= (count results-dead) 1))
        (is (= (.getProperty (first results-dead) "title") "Plant3"))))

    (component/stop system)))
