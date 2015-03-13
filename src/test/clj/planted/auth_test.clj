(ns planted.auth-test
  (:require [cemerick.friend :as friend]
            [clojure.test :refer :all]
            [clojure.tools.logging :as log]
            [compojure.core :refer (POST defroutes)]
            [planted.auth :refer :all]
            [ring.mock.request :as mock]
            [ring.util.response :refer [response]]))

(def test-config-basic
  {:use-basic-auth true})

(defroutes mock-routes
  (POST "/login" request
       (-> request
           friend/identity
           :current
           response)))

(def test-auth-handler
  (let [auth-handler (get-handler test-config-basic nil)]
    (-> mock-routes
        auth-handler)))

(deftest check-handler
  (testing "Basic auth check"
    (let [request (-> (mock/request "POST" "/login")
                      (mock/content-type "application/x-www-form-urlencoded")
                      (mock/body "username=admin&password=admin"))
          response (test-auth-handler request)]
      (log/info request)
      (log/info response)
      (is (= (:status response) 200)))))
