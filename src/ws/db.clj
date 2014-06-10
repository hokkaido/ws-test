(ns ws.db
  (:require
    [clj-time.format :as f]
    [ws.glue :as glue]
    [cheshire.core :refer :all]
    [clojure.java.io :as io]
    [datomic.api :as d])
  (:import datomic.Util))

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

(defn transact! [conn data-seq]
  (do-tx-helper conn :db.part/polis data-seq))

(defn setup-db
  [uri schema]
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (transact-all conn (io/resource schema))))

(defn db 
  [database]
  (d/db :connection database))

(defn get-councillor-by-name
  [database firstName]  
  (d/q '[:find ?e :where [?e :person/firstName firstName]] db database))

(defn save-councillor!
  [database councillor]
  (transact! (:connection database) [councillor]))






