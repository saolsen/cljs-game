(ns game.systems.graphics
  (:require [game.entities :as entities]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]))

(defn update-item
  "updates the key in objs with the result of (f oldval entity)"
  [objs key entity f]
  (let [old (get key objs)]
    (assoc objs key (f old entity))))

(defrecord GraphicsSystem [camera scene renderer objs]
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
        (when-not (contains? @objs key)
          (let [new (setup)]
            (.add scene new)
            (swap! objs assoc key (setup))))
        (swap! objs update-item key e update)))
    (.render renderer scene camera)))

(defn graphics-system
  []
  (map->GraphicsSystem (assoc (graphics/setup-threejs) :objs (atom {}))))