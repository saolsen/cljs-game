(ns game.systems.physics
  (:require [game.entities :as entities]
            [game.components :as components])
  (:use [game.systems :only [PSystem]]))
;; I'm going to deal with things in terms of acceleration and not in terms of
;; forces and masses. For the sake of this game we'll just assume everything is
;; massless and I won't deal with momentum or anything.

(defn add-velocity
  [position velocity td]
  (components/position
   (+ (:x position) (* (:x velocity) td))
   (+ (:y position) (* (:y velocity) td))
   (+ (:z position) (* (:z velocity) td))))

(defn swap-velocity [td entity]
  (let [id (first entity)
        comps (second entity)
        pos (entities/get-component comps :position)
        vel (entities/get-component comps :velocity)]
    [id (conj (entities/remove-component comps :position)
              (add-velocity pos vel td))]))

;; maybe use multiple physics systems, for now just moves along velocity
(defrecord PhysicsSystem []
  PSystem
  (components [_] #{:position :velocity})
  (setup [_] nil)
  (run [_ globals ents]
    (let [td (:delta globals)]
      (into {} (map (partial swap-velocity td) ents))))
)

(defn physics-system [] (PhysicsSystem.))