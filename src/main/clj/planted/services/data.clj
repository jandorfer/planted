(ns planted.services.data
  (:require [planted.db.access :as db])
  (:import [java.util UUID]))

(defn- set-uuid [data]
  (merge data {:uuid (.toString (UUID/randomUUID))}))

(defn- set-owner [owner data]
  (merge data {:owner owner}))

(defn get-plant
  "Fetches a single record by UUID."
  ([db rid] (get-plant db rid 1))
  ([db rid depth]
    (let [rid (if (.startsWith rid "#") rid (str "#" rid))]
      (db/get-v (:db db) "plant" rid depth))))

(defn create-plant!
  [db owner data]
  (->> data
       (set-uuid)
       (set-owner owner)
       (db/create! (:db db) "plant")
       (.getId)
       (.toString)
       (get-plant db)))

(defn- update-plant!
  "Use create-report! to make updates to plants."
  [db data]
  (let [prev (get-plant db (:rid data) 0)]
    (->> (merge prev data)
         (db/update! (:db db) (:rid data))
         (.getId)
         (.toString)
         (get-plant db))))

(defn create-report!
  [db owner data]
  (let [updates (:updates data)
        data (dissoc data :updates)
        report (->> data
                    (set-uuid)
                    (set-owner owner)
                    (db/create! (:db db) "report"))]
    (doseq [plant updates]
      (let [p-updated (update-plant! db plant)]
        (db/link! (:db db) p-updated report "update" plant)))))

(defn search-plants
  "Fetches all plants associated with the given user."
  [db owner]
  (db/search (:db db) "plant.owner" owner))


