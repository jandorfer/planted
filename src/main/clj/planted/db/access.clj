(ns planted.db.access
  (:require [clojure.tools.logging :as log]
            [planted.db.util :refer :all])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraph OrientVertex]
           (com.tinkerpop.blueprints Direction)))

(defn- clean-keys
  "Given a map which may have keys defined as keywords, ensures all such are
  converted to strings."
  [m]
  (zipmap
    (map name (keys m))
    (vals m)))

(defn parse-vertex
  "Parses a OreintDB vertex into a clojure map structure. Evaluates edges
  to the given depth."
  ([v] (parse-vertex v 1))
  ([v depth] (parse-vertex v depth {}))
  ([v depth out]
    (let [props (.getPropertyKeys v)
          ;edges (.getEdges v Direction/IN)
          p-obj (zipmap (map keyword props) (map #(.getProperty v %) props))]
      (merge out p-obj))))

(deftx create!
  "Creates a new Vertex of the given type, with the given properties. Note
  that the properties do not have to precisely match the type's predefined
  ones (though that is recommended) -- we are using a relaxed schema mode."
  [graph type props]
  (log/info "Creating" type)
  (let [^OrientVertex v (.addVertex graph (str "class:" type))]
    (.setProperties v (to-array (apply concat (clean-keys props))))
    v))

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
