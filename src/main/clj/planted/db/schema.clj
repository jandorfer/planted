(ns planted.db.schema
  (:require [planted.db.core :refer [deftx]])
  (:import [com.tinkerpop.blueprints.impls.orient OrientVertexType OrientVertex]
           [com.orientechnologies.orient.core.metadata.schema OType]))

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

(deftx create-plant-class [graph]
  (let [^OrientVertexType type (.createVertexType graph "Plant" "V")]
    (.createProperty type "title" (OType/STRING))
    (.createProperty type "planted" (OType/DATE))
    (.createProperty type "living" (OType/BOOLEAN))))

(deftx create-plant [graph title living]
  (let [^OrientVertex plant (.addVertex graph "class:Plant")]
    (.setProperties plant (to-array ["title" title "living" living]))
    plant))

(deftx get-plants [graph param value]
  (into [] (.getVertices graph param value)))

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

