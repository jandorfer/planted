{
  :uberjar {
    :aot :all
  }
  :dev {
    :resource-paths ["src/dev/resources"]
    :source-paths ["src/dev/clj"]
    :dependencies [[org.clojure/tools.namespace "0.2.7"]]
    :env {:db-url "memory:devdb"
          :db-admin-user nil
          :db-admin-pwd nil
          :bind 8081}
  }
  :test {
    :resource-paths ["src/dev/resources"]
    :dependencies [[clj-http "1.0.1"]]
    :env {:db-url "memory:testdb"
          :db-admin-user nil
          :db-admin-pwd nil
          :bind 8082}
  }
}