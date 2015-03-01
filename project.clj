(defproject planted "0.1.0-SNAPSHOT"
  :description "Planted, a tool to track growing things."
  :url "https://www.jasonandorfer.com/planted"
  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :resource-paths ["resources"]
  :repositories [["releases" "http://oss.sonatype.org/content/repositories/releases"] ;; Repos needed for orientdb
                 ["snapshots" "http://oss.sonatype.org/content/repositories/releases"]]
  :dependencies [[compojure "1.3.2"]
                 [com.orientechnologies/orient-commons "2.0-SNAPSHOT"]
                 [com.orientechnologies/orientdb-core "2.0-SNAPSHOT"]
                 [com.orientechnologies/orientdb-client "2.0-SNAPSHOT"]
                 [com.stuartsierra/component "0.2.2"]
                 [com.tinkerpop.blueprints/blueprints-orient-graph "2.5.0"]
                 [environ "1.0.0"]
                 [http-kit "2.1.19"]
                 [liberator "0.12.2"]
                 [log4j/log4j "1.2.15"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ring/ring-core "1.3.2"]]
  :plugins [[lein-environ "1.0.0"]]
  :main planted.system)
