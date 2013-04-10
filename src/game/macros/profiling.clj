(ns game.macros.profiling)

;; Must require this and also 
;; (:use [game.profiling :only [start-time! stop-time!]])

(defmacro profile
  "Wraps the body in the game.prifiling calls"
  [function-name & body]
  `(let [profile# (~(symbol "start-time!") ~function-name)
         result# ~@body]
     (~(symbol "stop-time!") profile#)
     result#))
