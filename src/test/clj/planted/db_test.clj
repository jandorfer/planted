(ns planted.db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [planted.db.access :refer [browse create! get-v link! search]]
            [planted.db.core :refer [new-database]]
            [planted.system :refer [planted]]
            [clojure.tools.logging :as log]))

(deftest test-db
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
        (is (= (:title (first results-dead)) "Plant3"))))

    (testing "Create report, link to Plant"
      (let [plant (create! db "Plant" {:title "My Plant"})
            report (create! db "Report" {:title "An update!"})]
        (link! db plant report "update" {:sprouted true})
        (let [plant1-with-relations (get-v db "Plant" (.getId plant) 2)]
          (log/debug plant1-with-relations)
          (is (= (count (:update plant1-with-relations)) 1)))))

    (testing "Multiple report links"
      (let [plant (create! db "Plant" {:title "My Other Plant"})
            report1 (create! db "Report" {:title "Some Report"})
            report2 (create! db "Report" {:title "Another Report"})]
        (link! db plant report1 "update" {:sprouted true})
        (link! db plant report2 "update" {:height 1.1})
        (let [plant1-with-relations (get-v db "Plant" (.getId plant) 2)]
          (log/debug plant1-with-relations)
          (is (= (count (:update plant1-with-relations)) 2)))))

    (component/stop db-c)))
