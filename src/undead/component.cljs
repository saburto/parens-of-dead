(ns undead.component
  (:require [reagent.dom :as rdom]))

(defn cell-component [id tile]
  [:div.cell {:key id :id id}
   [:div {:class (str "tile"
                      (when (:revealed? tile) " revealed")
                      (when (:matched? tile) " matched"))
          :on-click #((js/alert (str "Hello:" id)))
          }
    [:div.front]
    [:div {:class (str "back " (name (:face tile)))}]]])

(defn line-component [index tiles]
  (let [row-index (* 4 index)]
    [:div.line {:id row-index
                :key row-index}
     (map-indexed #(cell-component (+ row-index %1) %2) tiles)])
    )

(defn board-component [tiles]
  [:div {:class "board clearfix"}
   (map-indexed #(line-component %1 %2) (partition 4 tiles))])

(defn sand-component [index sand]
  [:div {:class (str "sand " (name sand)) :id index :key index}])

(defn timer-component [{:keys [sand index]}]
  [:div {:class (str "timer timer-" index) :id index :key index}
   (map-indexed sand-component sand)])

(defn timers-component [sand]
  (map-indexed #(timer-component {:index %1 :sand %2}) (partition 30 sand)))

(defn game-component [game]
  [:div {:class (when (:foggy? game) "foggy")}
   (board-component (:tiles game))
   (timers-component (:sand game))])

(defn render-game [game container]
  (rdom/render (game-component game)
               container))
