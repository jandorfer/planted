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
          :bind 8081
          :use-basic-auth true}
  }
  :test {
    :resource-paths ["src/dev/resources"]
    :dependencies [[clj-http "1.0.1"]
                   [ring/ring-mock "0.2.0"]]
    :env {:db-url "memory:testdb"
          :db-admin-user nil
          :db-admin-pwd nil
          :bind 8082
          :use-basic-auth true}
  }
}