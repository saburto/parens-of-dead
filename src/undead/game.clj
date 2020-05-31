(ns undead.game)

(def faces [:h1 :h1 :h2 :h2 :h3 :h3 :h4 :h4 :h5 :h5
            :fg :fg :zo :zo :zo :gy])

(defn- ->tile [face]
  {:face face})

(defn- update-tiles [game f]
  (update-in game [:tiles] #(mapv f %)))

(defn create-game []
  {:tiles (shuffle (map ->tile faces))
   :sand (repeat 30 :remaining)
   :foggy? false})

(defn- revealed-tiles [game]
  (->> game :tiles (filter :revealed?)))

(defn- can-reveal? [game]
  (> 2 (count (revealed-tiles game))))

(defn- match-reveal [tile]
  (if (:revealed? tile)
    (-> tile
        (assoc :matched? true)
        (dissoc :revealed?))
    tile))

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

(defn- wake-the-dead [tile]
  (if (= :gy (:face tile))
    (assoc tile :face :zo)
    tile))

(defn- perfom-match-actions [game match]
  (case match
    :fg (assoc game :foggy? true)
    :zo (-> game
            (update-in [:sand] #(replace-remaining % (repeat 3 :zoombie)))
            (update-tiles wake-the-dead))
    game))

(defn- check-for-match [game]
  (if-let [match (get-match game)]
    (-> game
        (update-tiles match-reveal)
        (perfom-match-actions match))
      game))

(defn- init-cancelment [tile]
  (if (:revealed? tile)
    (assoc tile :conceal-countdown 5)
    tile))

(defn- check-for-concealment [game]
  (if-not (can-reveal? game)
    (update-tiles game init-cancelment)
    game))

(defn reveal-tile [game index]
  (if (can-reveal? game)
    (-> game
        (assoc-in [:tiles index :revealed?] true)
        (check-for-match)
        (check-for-concealment))
    game))

(defn- assoc-ids [tiles]
  (map-indexed #(assoc %2 :id %1) tiles))

(defn- hide-faces [tile]
  (if (or (:revealed? tile)
          (:matched? tile)
          (:conceal-countdown tile))
    tile
    (dissoc tile :face)))

(defn prep [game]
  (-> game
      (update-in [:tiles] assoc-ids)
      (update-tiles hide-faces)))

(defn- conceal-face [tile]
  (case (:conceal-countdown tile)
    nil tile
    3 (-> tile (dissoc :revealed?) (update :conceal-countdown dec))
    1 (dissoc tile :conceal-countdown)
    (update tile :conceal-countdown dec)))

(defn tick [game]
  (-> game
      (update-tiles conceal-face)))
