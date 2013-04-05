(ns game.systems.graphics
  (:require [game.entities :as entities]
            [game.components :as components]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]
        [game.utils :only [log]]))

(defn update-item
  "updates the key in objs with the result of (f oldval entity)"
  [objs key entity f]
  (let [old (get key objs)]
    (assoc objs key (f old entity))))

;; This and the PlayerSystem are both big and coupled. Not sure if I
;; can fix that though.
(defrecord GraphicsSystem [camera scene renderer stats player-setup? objs]
  PSystem
  (components [_] #{:renderable})
  (setup [_]
    nil)
  (run [_ globals ents]
    (let [return (atom {})]

      ;; Process each entitiy.
      (doseq [e ents]
        (let [key (first e)
              comps (second e)
              {:keys [setup update]}
              (entities/get-component comps :renderable)]

          ;; Some special case stuff for the player.
          ;; Set up the camera objects.
          (when-not @player-setup?
            (when (entities/get-component comps :player)
              (log "adding camera to player")
              (let [cam (graphics/camera-objects camera)]
                (.add scene (:yaw-object cam))
                (swap! return
                       assoc
                       key
                       (conj comps (components/camera
                                    (:pitch-object cam)
                                    (:yaw-object cam)))))
              (reset! player-setup? true)))

          ;; Add renderable threejs objects if they aren't in our map.
          (when-not (contains? @objs key)
            (let [new (setup)]
              (.add scene new)
              (swap! objs assoc key new)))

              ;; Update threejs object with the update function.
              (swap! objs update-item key e update)))

      ;; update the screen
      (.update stats)
      (.render renderer scene camera)

      ;; return any updated entities
      @return))
  )

(defn graphics-system
  []
  (map->GraphicsSystem (assoc (graphics/setup-threejs)
                         :player-setup? (atom false)
                         :objs (atom {}))))
