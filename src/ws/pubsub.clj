(ns ws.pubsub
  (:require [cljs.core.async :refer [chan <! >! timeout pub sub unsub unsub-all]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def councillor-queue (chan))

(def councillor-item
  (pub councillor-queue :type))


(def retrieve-councillor-details (chan))

(sub councillor-queue :details retrieve-councillor-details)

