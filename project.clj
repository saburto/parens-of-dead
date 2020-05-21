(defproject parens-of-dead "0.1.0-SNAPSHOT"
  :description "Parens of dead game practice for clojure"
  :url "http://github.com/saburto/parens-of-dead"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main undead.system
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [http-kit "2.3.0"]
                 [com.stuartsierra/component "1.0.0"]
                 ]
  :target-path "target/%s"
  :profiles {:dev {:plugins []
                   :dependencies [[reloaded.repl "0.2.4"]]
                   :source-paths ["dev"]}})
