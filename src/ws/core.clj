(ns ws.core
  (:require
    [ws.glue :as glue]
    [clojure.pprint :as pprint]
    [clojure.java.io :as io]
    [datomic.api :as d])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
;;  (spit (clojure.java.io/resource "ws/councillor.edn") (pr-str (schema/to-schema (schema/get-councillor 1)))))
;;(setup-db polis-uri)
;; (let [conn (d/connect polis-uri)
;;       councillor (schema/to-schema-coerce (schema/get-councillor 1))]
;;  (transact-polis conn [councillor] )
;;  (pprint/pprint (d/q '[:find ?e :where [?e :person/firstName "Paul"]] (d/db conn)))))
  (println "Hello, stranger"))



