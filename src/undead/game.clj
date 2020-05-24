(ns undead.game)

(def faces [:h1 :h1 :h2 :h2 :h3 :h3 :h4 :h4 :h5 :h5
            :fg :fg :zo :zo :zo :gy])


(defn- ->tile [face]
  {:face face})

(defn create-game []
  {:tiles (shuffle (map ->tile faces))
   :sand (repeat 30 :remaining)
   :foggy? false})

(defn- revealed-tiles [game]
  (->> game :tiles (filter :revealed?)))

(defn- can-reveal? [game]
  (> 2 (count (revealed-tiles game))))

(defn- match-reveal [tiles]
  (mapv (fn [tile]
          (if (:revealed? tile)
            (-> tile
                (assoc :matched? true)
                (dissoc :revealed?))
            tile
            )) tiles))

(defn- get-match [game]
  (let [rtiles (revealed-tiles game)]
    (when (and (= 2 (count rtiles))
               (= 1 (count (set (map :face rtiles)))))
      (:face (first rtiles)))))

(defn- replace-remaining [sand replacement]
  (concat
   (take-while (complement #{:remaining}) sand)
   replacement
   (->> (drop-while (complement #{:remaining}) sand)
        (drop (count replacement)))))

(defn- wake-the-dead [tiles]
  (mapv (fn[tile]
          (if (= :gy (:face tile))
            (assoc tile :face :zo)
            tile))
        tiles))

(defn- perfom-match-actions [game match]
  (case match
    :fg (assoc game :foggy? true)
    :zo (-> game
            (update-in [:sand] #(replace-remaining % (repeat 3 :zoombie)))
            (update-in [:tiles] wake-the-dead))
    game))

(defn- check-for-match [game]
  (if-let [match (get-match game)]
    (-> game
        (update-in [:tiles] match-reveal)
        (perfom-match-actions match))
      game))

(defn- init-cancelment [tiles]
  (mapv (fn [tile]
          (if (:revealed? tile)
            (assoc tile :canceal-countdown 3)
            tile))
        tiles))

(defn- check-for-canelment [game]
  (if-not (can-reveal? game)
    (update-in game [:tiles] init-cancelment)
    game))

(defn reveal-tile [game index]
  (if (can-reveal? game)
    (-> game
        (assoc-in [:tiles index :revealed?] true)
        (check-for-match)
        (check-for-canelment))
    game))

(defn- assoc-ids [tiles]
  (map-indexed #(assoc %2 :id %1) tiles))

(defn- hide-faces [tiles]
  (mapv (fn [tile]
          (if (or (:revealed? tile)
                  (:matched? tile))
            tile
            (dissoc tile :face)))
        tiles))

(defn prep [game]
  (-> game
      (update-in [:tiles] assoc-ids)
      (update-in [:tiles] hide-faces)))

(defn- cancel-faces [tiles]
  (mapv (fn [tile]
          (case (:canceal-countdown tile)
            nil tile
            1 (dissoc tile :canceal-countdown :revealed?)
            (update tile :canceal-countdown dec)))
        tiles))

(defn tick [game]
  (-> game
      (update-in [:tiles] cancel-faces)))
