(ns ws.schema
  (:require
    [clj-time.format :as f]
    [cheshire.core :refer :all]
    [datomic.api :as d]))



(def councillor-datomic-map 
  {:firstName :person/firstName
   :lastName :person/lastName
   :birthDate :person/birthDate
   :id :councillor/id})

(defn parse-date
  [s]
  (.toDate (f/parse (f/formatters :date-time-no-ms ) s)))

(def councillor-coercions
  {:person/birthDate #(parse-date %)})

(def datomic-schema
  (vals councillor-datomic-map))

(defn strip-nil
  "Remove k/v pairs from a map where v is nil."
  [m]
  (into {} (remove (comp nil? second) m)))



(defn get-councillor
  [id]
  (parse-string (slurp (clojure.java.io/resource "ws/councillor.json")) true))

(defn foo-merge
  [v merge-fn]
  (merge-fn v))

(defn foo [c s]
  (merge-with foo-merge c s)) 

(defn to-schema
  [councillor]
  (select-keys (clojure.set/rename-keys councillor councillor-datomic-map) datomic-schema))

(defn to-schema-coerce
  [councillor]
  (foo (to-schema councillor) councillor-coercions))




