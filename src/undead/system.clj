(ns undead.system
  (:require [org.httpkit.server :refer [run-server]]
            [com.stuartsierra.component :as component]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello UNDEAD 2!"})

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
