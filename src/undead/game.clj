(ns undead.game)

(def faces [:h1 :h1 :h2 :h2 :h3 :h3 :h4 :h4 :h5 :h5 :fg :fg :zo :zo :zo :gy])


(defn- ->tile [face]
  {:face face})

(defn create-game []
  {:tiles (shuffle (map ->tile faces))
   :sand (repeat 30 :remaining)})

(defn- revealed-tiles [game]
  (->> game :tiles (filter :revealed?)))

(defn- can-reveal? [game]
  (> 2 (count (revealed-tiles game)))
  )

(defn- match-reveal [tiles]
  (mapv (fn [tile]
          (if (:revealed? tile)
            (-> tile
                (assoc :matched? true)
                (dissoc :revealed?))
            tile
            )) tiles))

(defn- check-for-match [game]
  (let [rtiles (revealed-tiles game)]
    (if (and (= 2 (count rtiles))
             (= 1 (count (set (map :face rtiles)))))
      (update-in game [:tiles] match-reveal)
      game))
  )

(defn reveal-tile [game index]
  (if (can-reveal? game)
    (-> game
        (assoc-in [:tiles index :revealed?] true)
        (check-for-match))
    game))
