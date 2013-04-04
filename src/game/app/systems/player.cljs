(ns game.systems.player
  (:require [game.entities :as ent]
            [game.components :as comp])
  (:use [game.systems :only [PSystem]]
        [game.utils :only [log]]))

;; A system to handle player specific stuff.
(defrecord PlayerSystem [setup?]
  PSystem
  (components [_] #{:player})
  (setup [_] nil)
  (run [_ globals ents]
    ;; Set up player entity
    ;; May want to have loading stuff different from gameplay stuff.
    ;; (yeah, probably do)
    (when-not @setup?
      (reset! setup? true)
      {(ent/gen-id) [(comp/camera [0 0 0] [0 150 400])
                     (comp/player)
                     (comp/position)
                     (comp/velocity)
                     ;; Not sure what to render but need this component
                     ;; so that the graphics sytem gets this entity.
                     (comp/renderable (constantly nil)
                                      (fn [a b] a))]})
    
    ;; update player camera?

    
    )
  )

(defn player-system [] (PlayerSystem. (atom false)))
