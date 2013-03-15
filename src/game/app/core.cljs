(ns game.core
  (:require [game.entities :as ent]
            [game.components :as comp]
            [game.systems :as sys])
  (:use [game.systems.graphics :only [graphics-system]]
        [game.systems.physics :only [physics-system]]))

;; Have to have this be global so it's in scope of callbacks.
(def entities (atom {:best-guy [(comp/position -1100 0 0)
                                (comp/velocity 10 0 0)]}))
(def systems [(graphics-system)
              (physics-system)])

(def last-tick (atom 0))
(defn animation-loop [t]
  (.webkitRequestAnimationFrame js/window animation-loop)
  (let [time-delta (/ (- t @last-tick) 1000)
        globals {:delta time-delta :now t}]
    (reset! last-tick t)

    ;; call all systems with relevant data.
    (doseq [s systems]
      (let [needed-components (sys/components s)
            needed-entities
            (ent/get-with-components @entities needed-components)
            changed (sys/run s globals needed-entities)]
        (swap! entities ent/change-entities changed)))))

(defn main []
  ;; setup systems
  (doseq [s systems]
    (sys/setup s))

  ;; main loop
  (let [start-time (.now js/Date)]
    (animation-loop start-time)))

(main)
