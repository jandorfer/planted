(ns planted.auth-test
  (:require [clojure.test :refer :all]
            [planted.auth :refer :all]
            [ring.mock.request :as mock]
            [clojure.tools.logging :as log]))

(def test-config-basic
  {:use-basic-auth true})

(defn get-test-auth-handler []
  (get-handler test-config-basic nil))

(deftest check-handler
  (testing "Auth handler redirect"
    (let [response ((get-test-auth-handler) (mock/request "POST" "/login"))]
      (log/info response)
      (is (= (:status response)
             200)))))
