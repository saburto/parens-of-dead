(ns undead.web
  (:require [chord.http-kit :refer [with-channel]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [clojure.core.async :refer [>! <! go]]
            [undead.game :refer [create-game reveal-tile]]))

(defn- ws-handler [req]
  (with-channel req ws-channel
    (go
        (>! ws-channel (create-game)))))

(defroutes app
  (GET "/ws" [] ws-handler)
  (resources "/"))
