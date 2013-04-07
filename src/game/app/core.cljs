(ns game.core
  (:require [game.entities :as ent]
            [game.components :as comp]
            [game.systems :as sys])
  (:use [game.systems.graphics :only [graphics-system]]
        [game.systems.player :only [player-system
                                    camera-system
                                    controls-system
                                    player-movement-system]]
        [game.systems.scene :only [test-scene-system]]
        [game.systems.physics :only [physics-system]]
        [game.systems.stuff :only [stuff-system]]))

(defprotocol PApp
  (start [this] "starts the app")
  (stop [this] "stops the app"))

(declare animation-loop)

(defrecord App [entities systems last-tick]
  PApp
  (start [_]
    ;; Has to be top level for the animation callback
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

      ;; setup systems
      (doseq [s systems]
        (sys/setup s))
      
      ;; main loop
      (let [start-time (.now js/Date)]
        (animation-loop start-time)))
  )

(defn create-app [] (App. (atom {})
                          [(player-system)
                           (camera-system)
                           (controls-system)
                           (player-movement-system)
                           (test-scene-system)
                           (physics-system)
                           (graphics-system)
                           (stuff-system)]
                          (atom 0)))

(start (create-app))
