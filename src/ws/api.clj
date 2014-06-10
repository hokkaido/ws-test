(ns ws.api
  (:require 
    [clojure.java.io :as io]
    [org.httpkit.client :as http]
    [cheshire.core :as json]
    [plumbing.core :as p]
    [plumbing.graph :as graph]))

(def base-uri "http://ws.parlament.ch")

(def councillors-uri (str base-uri "/councillors"))

(def councils-uri (str base-uri "/councils"))

(def cantons-uri (str base-uri "/cantons"))

(def request-options {:timeout 1000
              :headers {"Accept" "text/json"}
              :query-params { :lang "de" :format "json"}})


(defn with-page-number [p]
  (assoc-in request-options [:query-params :pageNumber] p))


(defn parse-response [{:keys [status headers body error]}]
  (if error
    (println "Failed, exception: " error)
    (json/parse-string body true)))


(defn has-more-pages? [r]
  (:hasMorePages (last r)))


(defn make-request!
  [url page-number]
  (http/get url (with-page-number page-number)))


(def crawl-graph
  {:response       (p/fnk [url page-number]  (make-request! url page-number))
   :result         (p/fnk [response] (parse-response @response))
   :has-more-pages (p/fnk [result] (has-more-pages? result))})

(def crawl-eager (graph/compile crawl-graph))

(defn crawl! 
  ([url]
    (crawl-eager {:url url :page-number 1}))
  ([url page-number]
    (crawl-eager {:url url :page-number page-number})))

(defn get-councillor
  [id]
  (:result (crawl! (str councillors-uri "/" id))))

(defn get-councillors
  []
  (:result (crawl! councillors-uri)))

  