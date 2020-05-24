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
                 [org.clojure/clojurescript "1.10.764"]
                 [reagent "0.10.0"]
                 [expectations/clojure-test "1.2.1"]
                 [jarohen/chord "0.8.1"]
                 [org.clojure/core.async "1.2.603"]]
  :resource-paths ["resources" "target"]
  :clean-targets ^{:protect false} ["target/public"]
  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]}
  :profiles {:dev {:plugins [[jonase/eastwood "0.3.10"]]
                   :eastwood {:exclude-linters [:constant-test]}
                   :dependencies [[com.bhauman/rebel-readline-cljs "0.1.4"]
                                  [com.bhauman/figwheel-main "0.2.5"]
                                  [reloaded.repl "0.2.4"]]
                   :source-paths ["dev"]
                   }})
