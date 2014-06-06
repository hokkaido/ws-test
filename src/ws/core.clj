(ns ws.core
  (:require
    [ws.schema :as schema]
    [clojure.pprint :as pprint])
  (:gen-class))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
;;  (spit (clojure.java.io/resource "ws/councillor.edn") (pr-str (schema/to-schema (schema/get-councillor 1)))))
 (pprint/pprint (schema/to-schema-coerce (schema/get-councillor 1))))
