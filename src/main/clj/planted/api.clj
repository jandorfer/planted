(ns planted.api
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [compojure.core :refer [ANY]]
            [liberator.representation :as lib]
            [planted.resources :as r]
            [planted.services.data :as data]
            [clojure.tools.logging :as log]))

(defn parse-json [source]
  (if (string? source)
    (json/parse-string source true)
    (json/parse-stream (io/reader source) true)))

(defmethod lib/render-map-generic "application/json"
  [data _]
  (json/generate-string data {:pretty true}))

(defn get-api-routes [db]
  [(ANY "/data/plant" []
     (r/resource
       :base r/authenticated-base
       :allowed-methods [:get :post]
       :available-media-types ["application/json"]
       :handle-ok #(data/search-plants db (:user-id %))
       :post! #(->> (get-in % [:request :body])
                    (parse-json)
                    (data/create-plant! db (:user-id %))
                    (hash-map :result))
       :handle-created #(:result %)))

   (ANY "/data/plant/:rid" [rid]
     (r/resource
       :base r/authenticated-base
       :allowed-methods [:get]
       :available-media-types ["application/json"]
       :exists? (fn [_]
                  (if-let [res (data/get-plant db rid)]
                    (do (log/info res) [true {:existing res}])
                    false))
       :handle-ok #(:existing %)))

   (ANY "/data/report" []
     (r/resource
       :base r/authenticated-base
       :allowed-methods [:post]
       :available-media-types ["application/json"]
       :post! #(->> (get-in % [:request :body])
                    (parse-json)
                    (data/create-report! db (:user-id %)))))])