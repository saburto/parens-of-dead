(ns undead.web
  (:require [chord.http-kit :refer [with-channel]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [clojure.core.async :refer [>! <! go]]
            [undead.game :refer [create-game prep reveal-tile]]))

(defn- ws-handler [req]
  (with-channel req ws-channel
    (go
      (loop [game (create-game)]
        (>! ws-channel (prep game))
        (when-let [tile-index (:message (<! ws-channel))]
          (recur (reveal-tile game tile-index)))))))

(defroutes app
  (GET "/ws" [] ws-handler)
  (resources "/"))
