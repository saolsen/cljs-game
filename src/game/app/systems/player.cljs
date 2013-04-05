(ns game.systems.player
  (:require [game.entities :as ent]
            [game.components :as comp]
            [game.graphics :as graphics])
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
      (log "adding player")
      (reset! setup? true)
      (let [cam-objects (graphics/camera-objects)]
        ;; base player entity, a camera gets added by the graphics system
        {(ent/gen-id) [(comp/player)
                       (comp/position)
                       (comp/velocity)
                       ;; Not sure what to render but need this component
                       ;; so that the graphics sytem gets this entity.
                       (comp/renderable (constantly nil)
                                        (fn [a b] a))]}))
    
    ;; Update player, assumption here is that there is only one.
    ;; Not sure if that is always going to be a valid assumption.
    ;; (when-let [[key comps] (first ents)]
    ;;   ;; update player camera?
    ;;   ;; Lets try this as a test?
    ;;   (let [cam (ent/get-component comps :camera)
    ;;         yaw (:yaw-object cam)
    ;;         pitch (:pitch-object)
    ;;         y (.-y (.-rotation yaw))
    ;;         p (.-y (.-rotation pitch))]
    ;;     (aset (.-rotation yaw) "y" (- y 1))
    ;;     (aset (.-rotation pitch) "y" (- p 1))))
    )
  )

(defn player-system [] (PlayerSystem. (atom false)))
