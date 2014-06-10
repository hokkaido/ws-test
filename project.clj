(defproject ws "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.3.1"]
                 [clj-time "0.7.0"]
                 [prismatic/plumbing "0.3.1"]
                 [com.stuartsierra/component "0.2.1"]
                 [sonian/carica "1.1.0"]
                 [http-kit "2.1.18"]
                 [com.datomic/datomic-free "0.9.4766.16"]]
  :main ^:skip-aot ws.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
