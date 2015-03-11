(ns planted.api
  (:require [compojure.core :refer [ANY]]
            [planted.db.access :refer :all]
            [planted.resources :as r]))

(defn get-api-routes [db]
  [(ANY "/data/:type" [type]
     (r/resource
       :base r/authenticated-base
       :allowed-methods [:get]
       :available-media-types ["application/json"]
       :handle-ok #(search (:db db) (str type ".owner") (:user-id %))))])