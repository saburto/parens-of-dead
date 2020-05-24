(ns undead.client
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [<!]]
            [undead.component :refer [render-game]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def main-container (.getElementById js/document "main"))

(js/console.log "Hello")

(defonce run-once ;; not every figwheel reload
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:9009/ws"))]
      (when error (throw error))

      (loop []
        (when-let [game (:message (<! ws-channel))]
          (render-game game main-container ws-channel)
          (recur))))))
