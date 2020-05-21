(ns undead.system
  (:require [org.httpkit.server :refer [run-server]]
            [undead.web :refer [app]]
            [com.stuartsierra.component :as component]))

(defn- start-server [handler port]
  (let [server (run-server handler {:port port})]
    (print (str "starting server:" port))
    server))

(defn- stop-server [server]
  (when server
    (server)))

(defrecord ParensOfTheDead []
  component/Lifecycle
  (start [this]
    (assoc this :server (start-server #'app 9009)))
  (stop [this]
    (stop-server (:server this))
    (dissoc this :server)))

(defn create-system []
  (ParensOfTheDead.))

(defn -main [& args]
  (.start (create-system)))
