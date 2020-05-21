(defproject parens-of-dead "0.1.0-SNAPSHOT"
  :description "Parens of dead game practice for clojure"
  :url "http://github.com/saburto/parens-of-dead"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main undead.system
  :dependencies [[com.stuartsierra/component "1.0.0"]
                 [compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.764"]]
  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.8"]
                             [lein-figwheel "0.5.20"]]
                   :dependencies [[reloaded.repl "0.2.4"]]
                   :source-paths ["dev"]
                   :cljsbuild {:builds [{:id "parens-of-dead"
                                         :source-paths ["src" "dev"]
                                         :figwheel true
                                         :compiler {:output-to "target/classes/public/app.js"
                                                    :output-dir "target/classes/public/out"
                                                    :asset-path "out"
                                                    :main undead.client
                                                    :optimizations :none
                                                    :recompile-dependents true
                                                    :source-map-timestamp true}}]}}})
