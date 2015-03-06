(ns planted.handler-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [environ.core :as config]
            [clj-http.client :as http]
            [planted.system :refer [planted]]))

(defn build-url [path]
  (str "http://localhost:" (:bind config/env) "/" path))

(deftest test-app
  (let [system (component/start (planted config/env))]

    (testing "Planted Home Page"
      (let [response (http/get (build-url "") {:throw-exceptions false})]
        (is (= (:status response) 200))))

    (testing "Deeper Link"
      (let [response (http/get (build-url "plant/abc123") {:throw-exceptions false})]
        (is (= (:status response) 200))))

    (testing "Missing Resource (js)"
      (let [response (http/get (build-url "js/missing") {:throw-exceptions false})]
        (is (= (:status response) 404))))

    (testing "Missing Resource (css)"
      (let [response (http/get (build-url "css/missing") {:throw-exceptions false})]
        (is (= (:status response) 404))))

    (testing "Missing Resource (img)"
      (let [response (http/get (build-url "img/missing") {:throw-exceptions false})]
        (is (= (:status response) 404))))

    (component/stop system)))
