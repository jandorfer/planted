(ns planted.db.access
  (:require [clojure.tools.logging :as log]
            [planted.db.util :refer :all])
  (:import [com.tinkerpop.blueprints.impls.orient OrientGraph OrientVertex]))

(defn- clean-keys
  "Given a map which may have keys defined as keywords, ensures all such are
  converted to strings."
  [m]
  (zipmap
    (map name (keys m))
    (vals m)))

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
  (into [] (.getVerticesOfClass graph class)))

(deftx search
   "Searches for all vertices of any type that have the given value for a
   parameter with the given name. Fetches eagerly."
   [graph param value]
   (into [] (.getVertices graph param value)))
