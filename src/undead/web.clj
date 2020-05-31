(ns undead.web
  (:require [chord.http-kit :refer [with-channel]]
            [compojure.core :refer [defroutes GET]]
            [undead.game-loop :refer [start-game-loop]]
            [compojure.route :refer [resources]]))

(defn- ws-handler [req]
  (with-channel req ws-channel
    (start-game-loop ws-channel)))

(defroutes app
  (GET "/ws" [] ws-handler)
  (resources "/"))
