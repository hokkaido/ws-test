(ns ws.system
  (:require
    [com.stuartsierra.component :as component]
    [datomic.api :as d]
    [ws.db :as db]))


(defrecord Database [uri schema]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [component]
    (println ";; Starting database")
    ;; In the 'start' method, initialize this component
    ;; and start it running. For example, connect to a
    ;; database, create thread pools, or initialize shared
    ;; state.
    (db/setup-db uri schema)
    (let [conn (d/connect uri)]
      ;; Return an updated version of the component with
      ;; the run-time state assoc'd in.      
      (assoc component :connection conn)))

  (stop [component]
    (println ";; Stopping database")
    ;; In the 'stop' method, shut down the running
    ;; component and release any external resources it has
    ;; acquired.
    (.release :connection)
    ;; Return the component, optionally modified.
    component))


(defn new-database [uri schema]
  (map->Database {:uri uri :schema schema}))


(defrecord ParliamentCrawler [interval]
  component/Lifecycle
  (start [component]
    (println ";; Starting parliament webservice crawler "))
  (stop [component]
    (println ";; Stopping parliament webservice crawler")))


(defn new-parliament-crawler [crawl-interval]
  (map->ParliamentCrawler {:crawl-interval crawl-interval}))


(def system-components [:db :parliament-crawler])

(defrecord MainSystem [config db parliament-crawler]
  component/Lifecycle
  (start [system]
    (component/start-system system system-components))
  (stop [system]
    (component/stop-system system system-components)))

(defn new-system [config]
  (map->MainSystem
    {:config config
     :db (new-database (:uri (:db config)) (:schema (:db config)))
     :parliament-crawler (new-parliament-crawler (:crawl-interval (:parliament config)))
    }))


