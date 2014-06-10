(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer (javadoc)]
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [com.stuartsierra.component :as component]
   [ws.system :as ws-system]
   [ws.db :as ws-db]
   [ws.api :as ws-api]
   [ws.glue :as ws-glue]))

(def my-system (ws-system/new-system 
  {:db 
   {:uri "datomic:mem://polis"
    :schema "schema/polis.edn"}
   :parliament 
    {:crawl-interval 30 }}))

(defn start
  "Starts the system running, updates the Var #'system."
  []
  (println "OMG")
  (alter-var-root #'my-system component/start)
  )

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  (alter-var-root #'my-system component/stop)
  )

(defn go
  "Initializes and starts the system running."
  []
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))