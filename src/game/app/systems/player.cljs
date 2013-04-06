(ns game.systems.player
  (:require [game.entities :as ent]
            [game.components :as comp]
            [game.graphics :as graphics]
            [game.math :as math])
  (:use [game.systems :only [PSystem]]
        [game.utils :only [log]]))

;; A system to handle player specific stuff.
(defrecord PlayerSystem [setup?]
  PSystem
  (components [_] #{:player})
  (setup [_] nil)
  (run [_ globals ents]
    ;; Set up player entity
    (when-not @setup?
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
    )
  )

(defn player-system [] (PlayerSystem. (atom false)))


;; Controls stuff
(defn on-mouse-move
  "mouse move handler, takes an atom reference,
   the pitch and yaw objects of the camera and
   the mousemove event."
  [enabled? yaw-object pitch-object event]
  (when @enabled?
    (let [move-x (.-webkitMovementX event)
          move-y (.-webkitMovementY event)]
      (when move-x
        (let [old (.-y (.-rotation yaw-object))
              new (- old (* move-x 0.002))]
          (aset (.-rotation yaw-object) "y" new)))
      (when move-y
        (let [old (.-x (.-rotation pitch-object))
              new (- old (* move-y 0.002))
              real (max (- math/PI-2) (min math/PI-2 new))]
          (aset (.-rotation pitch-object) "x" real)))
      nil)))

(defn pointer-lock-change
  "pointer lock change callback,
   takes an enabled? atom that it sets."
  [enabled? event]
  (if (= (js/getcanvas) (.-webkitPointerLockElement js/document))
    (reset! enabled? true)
    (reset! enabled? false)))

(defn canvas-click
  "click handler for the canvas element"
  [event]
  (let [canvas (js/getcanvas)]
    (.webkitRequestPointerLock canvas)))

;; Deals with player controls and movement.
(defrecord ControlsSystem [setup? enabled?]
  PSystem
  (components [_] #{:camera})
  (setup [_] )
  (run [_ globals ents]
    (let [[key comps] (first ents)]
      (when-let [cam (ent/get-component comps :camera)]
      
      ;; Set up on first pass
        (when-not @setup?
          (let [mouse-handler (partial on-mouse-move
                                 enabled?
                                 (:yaw-object cam)
                                 (:pitch-object cam))
                pointer-handler (partial pointer-lock-change
                                         enabled?)]
            ;; handlers
            (.addEventListener js/document "mousemove"
                               mouse-handler false)
            (.addEventListener js/document "webkitpointerlockchange"
                               pointer-handler false)
            ;; pointerlock
            (.addEventListener (js/getcanvas) "click" canvas-click false))
          (reset! setup? true))
        nil
        )))
  )

(defn controls-system [] (ControlsSystem. (atom false) (atom false)))
