(defproject planted "0.1.0-SNAPSHOT"
  :description "Planted, a tool to track growing things."
  :url "https://www.jasonandorfer.com/planted"
  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :resource-paths ["resources"]
  :repositories [["releases" "https://oss.sonatype.org/content/repositories/releases"] ;; Repos needed for orientdb
                 ["snapshots" "https://oss.sonatype.org/content/repositories/snapshots"]]
  :dependencies [[ch.qos.logback/logback-classic "1.1.2"]
                 [cheshire "5.4.0"]
                 [compojure "1.3.2"]
                 [com.cemerick/friend "0.2.1"
                  ;; Newer version of these are in friend-oauth2
                  :exclusions [commons-codec
                               commons-logging
                               org.apache.httpcomponents/httpclient
                               org.apache.httpcomponents/httpcore
                               slingshot]]
                 [com.orientechnologies/orient-commons "2.0-M1"]
                 [com.orientechnologies/orientdb-client "2.0.4"]
                 [com.orientechnologies/orientdb-graphdb "2.0.4"]
                 [com.stuartsierra/component "0.2.2"]
                 [environ "1.0.0"]
                 [friend-oauth2 "0.1.3"]
                 [http-kit "2.1.19"]
                 [liberator "0.12.2"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 ;; OrientDB uses java logging, this pulls that in to our logback system
                 [org.slf4j/jul-to-slf4j "1.7.10" :exclusions [org.slf4j/slf4j-api]]
                 [ring/ring-core "1.3.2"]]
  :plugins [[lein-environ "1.0.0"]]
  :main planted.system)
