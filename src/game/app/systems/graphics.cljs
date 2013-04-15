(ns game.systems.graphics
  (:require [game.entities :as entities]
            [game.components :as components]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]
        [game.utils :only [log]]))

;; This and the PlayerSystem are both big and coupled. Not sure if I
;; can fix that though.
(defrecord GraphicsSystem [camera scene renderer stats player-setup? objs]
  PSystem
  (components [_] #{:renderable :position})
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
              (let [cam (graphics/camera-objects camera)]
                (.add scene (:yaw-object cam))
                (swap! return
                       assoc
                       key
                       ;;FIXME: can't use conj when I change the
                       ;;entities implementation.
                       (conj comps (components/camera
                                    (:pitch-object cam)
                                    (:yaw-object cam)))))
              (reset! player-setup? true)))

          ;; Add renderable threejs objects if they aren't in our map.
          (when-not (aget objs key)
            (let [new (setup)]
              (.add scene new)
              (aset objs key new)))

          ;; Update threejs object with the update function.
          (let [oldval (aget objs key)
                newval (update oldval e)]
            (aset objs key newval))

          ;; Update positions
          (when-let [obj (aget objs key)]
            (let [old-pos (.-position obj)
                  oldx (.-x old-pos)
                  oldy (.-y old-pos)
                  oldz (.-z old-pos)
                  position (entities/get-component comps :position)
                  newx (:x position)
                  newy (:y position)
                  newz (:z position)]
              (when-not (and (= oldx newx)
                             (= oldy newy)
                             (= oldz newz))
                (.set old-pos newx newy newz))))
          
          ))

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
                         :objs (clj->js {}))))
