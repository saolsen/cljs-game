(ns game.math)

(defn random
  "Random float between 0 and 1"
  []
  (js/Math.random))

(defn rand-int
  "Random integer between begin and end"
  [min max]
  (+ (js/Math.floor (* (random) (+ (- max min) 1))) min))
