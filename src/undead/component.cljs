(ns undead.component
  (:require [cljs.core.async :refer [put!]]
            [reagent.dom :as rdom]))

(defn cell-component [tile reveal-ch]
  (let [id (:id tile)]
    [:div.cell {:key id :id id}
     [:div {:class (str "tile"
                        (when (:revealed? tile) " revealed")
                        (when (:matched? tile) " matched"))
            :on-click #(put! reveal-ch id)
            }
      [:div.front]
      [:div {:class (str "back " (when (:face tile)
                                   (name (:face tile))))}]]]))

(defn line-component [index tiles reveal-ch]
  (let [row-index (* 4 index)]
    [:div.line {:key row-index}
     (map-indexed #(cell-component %2 reveal-ch) tiles)]))

(defn board-component [tiles reveal-ch]
  [:div {:class "board clearfix"}
   (map-indexed #(line-component %1 %2 reveal-ch) (partition 4 tiles))])

(defn sand-component [index sand]
  [:div {:class (str "sand " (name sand)) :id index :key index}])

(defn timer-component [{:keys [sand index]}]
  [:div {:class (str "timer timer-" index) :id index :key index}
   (map-indexed sand-component sand)])

(defn timers-component [sand]
  (map-indexed #(timer-component {:index %1 :sand %2}) (partition 30 sand)))

(defn game-component [game reveal-ch]
  [:div {:class (when (:foggy? game) "foggy")}
   (board-component (:tiles game) reveal-ch)
   (timers-component (:sand game))])

(defn render-game [game container reveal-ch]
  (rdom/render (game-component game reveal-ch)
               container))
