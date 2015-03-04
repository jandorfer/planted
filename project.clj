(defproject planted "0.1.0-SNAPSHOT"
  :description "Planted, a tool to track growing things."
  :url "https://www.jasonandorfer.com/planted"
  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :resource-paths ["resources"]
  :repositories [["releases" "https://oss.sonatype.org/content/repositories/releases"] ;; Repos needed for orientdb
                 ["snapshots" "https://oss.sonatype.org/content/repositories/snapshots"]]
  :dependencies [[ch.qos.logback/logback-classic "1.1.2"]
                 [compojure "1.3.2"]
                 [com.orientechnologies/orient-commons "2.0-M1"]
                 [com.orientechnologies/orientdb-client "2.0.4"]
                 [com.orientechnologies/orientdb-graphdb "2.0.4"]
                 [com.stuartsierra/component "0.2.2"]
                 [environ "1.0.0"]
                 [http-kit "2.1.19"]
                 [liberator "0.12.2"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/jul-to-slf4j "1.7.10"]          ;; OrientDB uses java logging
                 [ring/ring-core "1.3.2"]]
  :plugins [[lein-environ "1.0.0"]]
  :main planted.system)
