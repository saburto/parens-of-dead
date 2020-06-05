(ns undead.game-test
  (:require [undead.game :refer :all]
            [clojure.test :refer [deftest is]]
            [expectations.clojure.test :refer [expect]]))

(defn- find-face-index [game face]
  (first (keep-indexed (fn [index tile]
                   (when (and (= face (:face tile))
                              (not (:revealed? tile)))
                     index))
                 (:tiles game))))

(defn reveal-one [face game]
  (reveal-tile game (find-face-index game face)))

(deftest create-game-test
  (expect
   {:h1 2 :h2 2 :h3 2 :h4 2 :h5 2 :fg 2 :zo 3 :gy 1}
   (->> (create-game) :tiles (map :face) frequencies)))

(deftest create-different-games
  (expect #(< 10 %) (count (set (repeatedly 100 create-game)))))

(deftest sand-initial-value
  (expect {:remaining 30} (frequencies (:sand (create-game)))))

(deftest revael-one-tile
  (expect 1 (->> (reveal-tile (create-game) 0)
                 :tiles (filter :revealed?) count)))

(deftest two-reveal
  (expect [:h1 :h2]
          (->> (create-game)
               (reveal-one :h1)
               (reveal-one :h2)
               (reveal-one :h3)
               :tiles
               (filter :revealed?)
               (map :face)
               (sort))))

(deftest matching-pair
  (expect [:h1 :h1]
          (->> (create-game)
               (reveal-one :h1)
               (reveal-one :h1)
               :tiles
               (filter :matched?)
               (map :face))))

(deftest after-matched-one-revealed
  (expect [:h3]
          (->> (create-game)
               (reveal-one :h1)
               (reveal-one :h1)
               (reveal-one :h3)
               :tiles
               (filter :revealed?)
               (map :face))))

(deftest foggy-game
  (expect (->> (create-game)
                    (reveal-one :fg)
                    (reveal-one :fg)
                    :foggy?)))

(deftest zoombies-in-sand
  (expect [:zoombie :zoombie :zoombie :remaining] (->> (create-game)
               (reveal-one :zo)
               (reveal-one :zo)
               :sand
               (take 4))))

(deftest more-zoombies
  (expect
   {:h1 2 :h2 2 :h3 2 :h4 2 :h5 2 :fg 2 :zo 4}
   (->> (create-game)
        (reveal-one :zo)
        (reveal-one :zo)
        :tiles
        (map :face)
        frequencies)))

(deftest add-id-faces
  (expect {nil 16}
          (->> (create-game)
               prep
               :tiles
               (map :face)
               frequencies)))

(deftest prep-after-reveal-on
  (expect {nil 15, :h1 1}
          (->> (create-game) (reveal-one :h1)
               prep :tiles (map :face) frequencies)))

(deftest prep-after-reveal-two
  (expect {nil 14, :h1 2}
          (->> (create-game) (reveal-one :h1) (reveal-one :h1)
               prep :tiles (map :face) frequencies)))

(deftest prep-ids
  (expect (range 0 16)
          (->> (create-game) prep :tiles (map :id))))

(deftest tick-cancelment
  (expect 0 (->> (create-game)
                 (reveal-one :h1)
                 (reveal-one :h2)
                 tick tick tick
                 :tiles
                 (filter :revealed?)
                 count)))

(defn tick-n [n game]
  (first (drop n (iterate tick game))))

(deftest gone-sand
  (expect [:gone :remaining]
          (->> (create-game)
               (tick-n 5)
               :sand (take 2))))


(deftest all-sand-to-be-gone
  (expect {:gone 30}
          (->> (create-game)
               (tick-n 150)
               :sand frequencies)))

(deftest dead-when-all-sand-gone
  (expect (->> (create-game)
               (tick-n 155)
               :dead?)))

(defn- reveal-two [face game]
  (->> game
       (reveal-one face)
       (reveal-one face)))

(defn- reveal-all-houses [game]
  (->> game
       (reveal-two :h1)
       (reveal-two :h2)
       (reveal-two :h3)
       (reveal-two :h4)
       (reveal-two :h5)))

(deftest reveal-all-houses-go-to-safe
  (expect (not (->> (create-game)
                   (reveal-all-houses)
                   tick tick
                   :safe?))))

(deftest second-round
  (expect 60 (->> (create-game)
                   (reveal-all-houses)
                   tick tick tick
                   :sand
                   count)))

(deftest thrid-round
  (expect 90 (->> (create-game)
                  (reveal-all-houses) tick tick tick
                  (reveal-all-houses) tick tick tick
                  :sand
                  count)))

(deftest after-three-round-safe
  (expect (->> (create-game)
                  (reveal-all-houses) tick tick tick
                  (reveal-all-houses) tick tick tick
                  (reveal-all-houses) tick tick tick
                  :safe?)))

(deftest second-round-board-empty
  (expect empty? (->> (create-game)
                  (reveal-all-houses)
                  tick tick tick
                  :tiles
                  (filter :matched?))))
