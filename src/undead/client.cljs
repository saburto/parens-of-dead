(ns undead.client
  (:require [reagent.dom :as rdom]))

(def game {:board [{:face :h1} {:face :h1} {:face :h2} {:face :h2 :revealed? true}
                   {:face :h3} {:face :h3} {:face :h4 :matched? true} {:face :h4 :matched? true}
                   {:face :h5} {:face :h5} {:face :fg} {:face :fg}
                   {:face :zo} {:face :zo :matched? true} {:face :zo :matched? true} {:face :gy}]
           :sand (concat (repeat 10 :gone)
                         (repeat 20 :remaining))
           :foggy? false})

(defn cell-component [cell]
  [:div.cell
   [:div {:class (str "tile"
                      (when (:revealed? cell) " revealed")
                      (when (:matched? cell) " matched"))}
    [:div.front]
    [:div {:class (str "back " (name (:face cell)))}]]])

(defn line-component [cells]
  [:div.line (map cell-component cells)])

(defn board-component [cells]
  [:div {:class "board clearfix"}
   (map line-component (partition 4 cells))])

(defn sand-component [sand]
  [:div {:class (str "sand " (name sand))}])

(defn timer-component [{:keys [sand index]}]
  [:div {:class (str "timer timer-" index)}
   (map sand-component sand)])

(defn timers-component [sand]
  (map-indexed #(timer-component {:index %1 :sand %2}) (partition 30 sand)))

(defn game-component [game]
  [:div {:class (when (:foggy? game) "foggy")}
   (board-component (:board game))
   (timers-component (:sand game))])

(rdom/render (game-component game)
             (.getElementById js/document "main"))
