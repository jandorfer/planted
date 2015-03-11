(ns planted.db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [planted.db.access :refer [browse create! search]]
            [planted.db.core :refer [new-database]]
            [planted.system :refer [planted]]))

(deftest test-app
  (let [db-url (get-in config/env [:db-url] "memory:testdb")
        db-admin-user (get-in config/env [:db-admin-user] nil)
        db-admin-pwd (get-in config/env [:db-admin-pwd] nil)
        db-c (component/start (new-database db-url db-admin-user db-admin-pwd))
        db (:db db-c)]

    (testing "Create and use Plant type"
      (doto db
        (create! "Plant" {:title "Plant1" :living true})
        (create! "Plant" {:title "Plant2" :living true})
        (create! "Plant" {:title "Plant3" :living false}))
      (let [results-living (search db "living" true)
            results-dead (search db "living" false)
            results-type (browse db "Plant")]
        (is (= (count results-living) 2))
        (is (= (count results-dead) 1))
        (is (= (count results-type) 3))
        (is (= (.getProperty (first results-dead) "title") "Plant3"))))

    (component/stop db-c)))
