(ns planted.db.access
  (:require [clojure.tools.logging :as log]
            [planted.db.util :refer :all])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraph OrientVertex OrientVertexType OrientEdge]
           (com.tinkerpop.blueprints Direction)))

(defn- clean-keys
  "Given a map which may have keys defined as keywords, ensures all such are
  converted to strings."
  [m]
  (zipmap
    (map name (keys m))
    (vals m)))

(defn- type-extends?
  "Determines if the given OrientVertexType is or descends from the given
  type name."
  [^OrientVertexType type name]
  (or (.equals (.getName type) name)
      (.isSubClassOf type name)))

(declare parse-vertex)

(defn parse-edge
  "Parses a OrientDB edge by expanding its properties into a clojure map, and
  loading the Vertex on the other end.

  The end result of parsing two related vertices in a map would be like:
  { title: 'Vertex 1'
    edge-name: [
      { edge-property: 'something'
        ref: { title: 'Vertex 2' }
    ] }"
  [^OrientEdge e depth]
  (let [props (.getPropertyKeys e)
        p-obj (zipmap (map keyword props) (map #(.getProperty e %) props))
        v (.getVertex e Direction/OUT)
        v-obj (parse-vertex v depth)]
    {(keyword (.getLabel e)) [(merge p-obj {:ref v-obj})]}))

(defn parse-vertex
  "Parses a OreintDB vertex into a clojure map structure. Evaluates edges
  to the given depth."
  ([^OrientVertex v] (parse-vertex v 1))
  ([^OrientVertex v depth]
    (let [props (.getPropertyKeys v)
          p-obj (zipmap (map keyword props) (map #(.getProperty v %) props))]
      (if (> depth 0)
        (let [child-depth (- depth 1)
              raw-edges (.getEdges v Direction/IN nil)
              edges (map parse-edge raw-edges (repeat child-depth))]
          (apply merge-with (concat [concat p-obj] edges)))))))

(deftx create!
  "Creates a new Vertex of the given type, with the given properties. Note
  that the properties do not have to precisely match the type's predefined
  ones (though that is recommended) -- we are using a relaxed schema mode."
  [graph type props]
  (log/info "Creating vertex" type)
  (let [^OrientVertex v (.addVertex graph (str "class:" type))]
    (.setProperties v (to-array (apply concat (clean-keys props))))
    v))

(deftx link!
  "Given two vertices, adds an edge linking them (OUT of second, IN to the
  first). The edge type and properties are given."
  [graph v-to v-from type props]
  (log/info "Creating edge" type)
  (let [fields (to-array (apply concat (clean-keys props)))]
    (.addEdge v-from type v-to nil nil fields)))

(deftx get-v
  "Retrieves the vertex with the given ID. Vertex type must match or inherit
  the given class name. Incoming edges are fetched to the given depth."
  [graph class id depth]
  (log/info "Fetching" class id)
  (when-let [vertex (.getVertex graph id)]
    (if (type-extends? (.getType vertex) class)
      (parse-vertex vertex depth))))

(deftx browse
  "Searches for all vertices of the given type. Fetches eagerly."
  [graph class]
  (log/info "Browsing:" class)
  (into [] (map parse-vertex (.getVerticesOfClass graph class))))

(deftx search
   "Searches for all vertices of any type that have the given value for a
   parameter with the given name. Fetches eagerly."
   [graph param value]
   (log/info "Searching:" param "=" value)
   (into [] (map parse-vertex (.getVertices graph param value))))
