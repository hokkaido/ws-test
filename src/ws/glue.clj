(ns ws.glue
  (:require
  [clj-time.format :as f]
  [cheshire.core :refer :all]
  [datomic.api :as d]))

(defn coercion-merge
  [v merge-fn]
  (merge-fn v))

(defn with-coercions [c s]
  (merge-with coercion-merge c s)) 

(defn parse-date
  [s]
  (.toDate (f/parse (f/formatters :date-time-no-ms ) s)))

(def councillor-rename-keys-map 
  {:firstName :person/firstName
   :lastName :person/lastName
   :birthDate :person/birthDate
   :id :councillor/id})

(def councillor-coercions
  {:person/birthDate #(parse-date %)})

(def councillor-db-schema
  (vals councillor-rename-keys-map))

(defn strip-nil
  "Remove k/v pairs from a map where v is nil."
  [m]
  (into {} (remove (comp nil? second) m)))

;;(defn get-councillor
;;  [id]
;;  (parse-string (slurp (clojure.java.io/resource "ws/councillor.json")) true))

(defn councillor-rename-keys
  [councillor]
  (select-keys (clojure.set/rename-keys councillor councillor-rename-keys-map) councillor-db-schema))

(defn councillor-to-db-schema
  [councillor]
  (with-coercions (councillor-rename-keys councillor) councillor-coercions))

