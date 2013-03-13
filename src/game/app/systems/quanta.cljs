(ns game.systems.quanta
  (:require [game.entities :as entities]
            [game.components :as components]
            [game.math :as math])
  (:use [game.systems :only [PSystem]]))

;; Spawn at -1100 x
(defrecord QCreationSystem [schedule]
  PSystem
  (components [_] #{})
  (setup [_] nil)
  (run [_ globals _]
    ;; now lets make these spawn maybe once every 5 seconds?
    (let [time (:now globals)
          delta (:delta globals)]
      (when (> time @schedule)
        (reset! schedule (+ 250 time))
        {(entities/gen-id)
         [(components/position -1500 (math/rand-int -100 100) 0)
          (components/velocity 500 0 0)]})))
)

(defn quanta-creation-system [] (QCreationSystem. (atom 0)))
