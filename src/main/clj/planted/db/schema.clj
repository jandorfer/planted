(ns planted.db.schema
  (:require [clojure.tools.logging :as log])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraphFactory OrientGraph OrientVertexType]
           [com.orientechnologies.orient.core.metadata.schema OType]))

(defn- init-vertex-type
  "Creates a vertex type definition in the database if not present."
  [^OrientGraphFactory db type parent properties]
  (let [graph (.getNoTx db)]
    (if-let [existing-type (.getVertexType graph type)]
      existing-type
      (do (log/info "Creating vertex type:" type)
          (let [otype (.createVertexType graph type parent)]
            (doseq [[prop ptype] (seq properties)]
              (log/debug type "- Creating property" prop "with data type" ptype)
              (.createProperty otype (name prop) ptype)))
          type))))

(defn init-schema!
  "Takes a map of schema definitions, and ensures the indicated types exist in
  the database."
  [^OrientGraphFactory db schema]
  (let [vertex-types (:vertex-types schema)
        edge-types (:edge-types schema)]
    (doseq [{:keys [name parent properties]} vertex-types]
      (init-vertex-type db name parent properties))
    (doseq [{:keys [name parent properties]} edge-types]
      (log/warn "TODO: Edges not implemented."))))

;; The Planted schema is designed around the Plant as the primary record, since
;; we are after all tracking plants. Since we're using a graph database
;; (OrientDB) where joins are cheap, we're going to break up data (normalize)
;; pretty heavily.

;; Plant is a Vertex with links to just about everything else. It represents,
;; well, a plant. Some sample properties:
;; - living (boolean)
;; - planted (date)
;; - health (0-10?)
;; Users can and are encouraged to make up their own properties.

(def owned-v
  {:name "Owned"
   :parent "V"
   :properties {:owner OType/STRING}})

(def plant
  {:name "Plant"
   :parent (:name owned-v)
   :properties {:title OType/STRING
                :planted OType/DATE
                :living OType/BOOLEAN}})

(def planted-schema
  {:vertex-types [owned-v plant]})

;; The other main thing to start are "Report"s for a given plant. This is how we
;; go about adding data to the record over time. In fact, a Plant is really just
;; an aggregate of all known reports about that plant. While a report itself is
;; just written information, the Edge (PlantUpdate) linking the report to the
;; plant contains new values for whatever properties on the Plant the user wants
;; to update. This information is stored in the edge, and the Plant record
;; updated. It can even add and remove properties, without losing the historical
;; record.

;; Plant Type is another vertex representing -- you guessed it -- a general type
;; of plant, for example a type for grape vines could be "Petite Pearl".

;; Site is a location where plants exists in the world. Geo-located is best
;; but not required. For example, "my vineyard" could be a site. For relative
;; positioning within a site, we'll store information in the edge connecting it
;; with a specific plant.

;; Owner is a person using the app. It could be extended to groups as well, but
;; for now intended to just represent a person. Owners are linked to pretty much
;; everything above.

