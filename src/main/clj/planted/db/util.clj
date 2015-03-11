(ns planted.db.util
  (:import [org.slf4j.bridge SLF4JBridgeHandler]))

(defmacro deftx
  "Writes a function with the given name which takes as its first argument an
  instance of the OrientGraph to work with. Remaining arguments will be passed
  through. The remaining actions (parameters 3+ to this macro) will comprise
  the body of the function to execute against the graph, and said execution
  will take place within the context of a transaction."
  [name & body]
  (let [doc? (string? (first body))
        doc (if doc? (first body) "")
        [[graph-arg & args] & body] (if doc? (rest body) body)]
    `(defn ~name
       ~doc
       ~(vec (cons 'db-conn-factory args))
       (let [~graph-arg (.getTx ~'db-conn-factory)]
         (try
           (let [result# (do ~@body)]
             (.commit ~graph-arg)
             result#)
           (catch Throwable t#
             (.rollback ~graph-arg)
             (throw t#))
           (finally
             (.shutdown ~graph-arg)))))))

(defn reset-logging-bridge
  "OrientDB uses java.util.logging, which we need to redirect to slf4j.
  This method ensures the logging is properly reset."
  []
  (SLF4JBridgeHandler/removeHandlersForRootLogger)
  (SLF4JBridgeHandler/install))