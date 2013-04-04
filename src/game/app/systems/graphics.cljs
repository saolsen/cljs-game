(ns game.systems.graphics
  (:require [game.entities :as entities]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]))

(defn update-item
  "updates the key in objs with the result of (f oldval entity)"
  [objs key entity f]
  (let [old (get key objs)]
    (assoc objs key (f old entity))))

(defrecord GraphicsSystem [camera scene renderer stats objs]
  PSystem
  (components [_] #{:renderable})
  (setup [_]
    nil)
  (run [_ globals ents]
      (doseq [e ents]
        (let [key (first e)
              comps (second e)
              {:keys [setup update]}
              (entities/get-component comps :renderable)]

          ;; Update the camera (if this is the player)
          (let [zpos (.-z (.-position camera))]
            (aset (.-position camera) "z" (- zpos 1)))

          ;; this be broke! ;;

          ;; (when-let [cam (entities/get-component comps :camera)]
          ;;   (.log js/console "dat cam")
          ;;   (.log js/console camera)
          ;;   (.log js/console (clj->js cam))
          ;;   (apply .set (.-position camera) (:pos cam))
          ;;   (apply .lookAt camera (:lookat cam))
          ;;   )

          ;; What if we just try rotating the camera or something a
          ;; bit each frame?

          ;; Add threejs object if it isn't in our objs map
          (when-not (contains? @objs key)
            (let [new (setup)]
              (.add scene new)
              (swap! objs assoc key new)))

          ;; Update threejs object with the update function.
          (swap! objs update-item key e update)))

    ;; update the screen
    (.update stats)
    (.render renderer scene camera)))

(defn graphics-system
  []
  (map->GraphicsSystem (assoc (graphics/setup-threejs) :objs (atom {}))))

;; So, if we had our way we'd have the camera system return pure coordinates on
