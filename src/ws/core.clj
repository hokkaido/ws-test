(ns ws.core
  (:require
    [ws.schema :as schema]
    [clojure.pprint :as pprint]
    [clojure.java.io :as io]
    [datomic.api :as d])
  (:import datomic.Util)
  (:gen-class))

(def polis-uri "datomic:mem://polis")

(defn read-all
  "Read all forms in f, where f is any resource that can
   be opened by io/reader"
  [f]
  (Util/readAll (io/reader f)))

(defn transact-all
  "Load and run all transactions from f, where f is any
   resource that can be opened by io/reader."
  [conn f]
  (doseq [txd (read-all f)]
    (d/transact conn txd))
  :done)

(defn- do-tx-helper [conn partition data-seq]
  (let [data
        (for [data data-seq]
          (assoc data :db/id (d/tempid partition)))]
    @(d/transact conn data)))

(defn transact-polis [conn data-seq]
  (do-tx-helper conn :db.part/user data-seq))


(defn setup-db
  [uri]
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (transact-all conn (io/resource "schema/polis.edn"))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
;;  (spit (clojure.java.io/resource "ws/councillor.edn") (pr-str (schema/to-schema (schema/get-councillor 1)))))
 (setup-db polis-uri)
 (let [conn (d/connect polis-uri)
       councillor (schema/to-schema-coerce (schema/get-councillor 1))]
  (transact-polis conn [councillor] )
  (pprint/pprint (d/q '[:find ?e :where [?e :person/firstName "Paul"]] (d/db conn)))))



