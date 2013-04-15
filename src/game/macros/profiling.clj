(ns game.macros.profiling)

;; Must require this and also 
;; (:use [game.profiling :only [start-time! stop-time!]])

(defmacro profile
  "Wraps the body in the game.profiling calls"
  [function-name & body]
  `(let [profile# (~(symbol "game.profiling.start-time!") ~function-name)
         result# (do ~@body)]
     (~(symbol "game.profiling.stop-time!") profile#)
     result#))
