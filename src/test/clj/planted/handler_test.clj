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

    (testing "Planted Home"
      (let [response (http/get (build-url "planted") {:throw-exceptions false})]
        (is (= (:status response) 200))))

    (testing "Missing Resource"
      (let [response (http/get (build-url "resource/missing") {:throw-exceptions false})]
        (is (= (:status response) 404))))

    (component/stop system)))
