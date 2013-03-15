(ns game.systems.graphics
  (:require [game.entities :as entities]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]))

;; for now render everything as quanta
(defrecord GraphicsSystem [camera scene renderer]
  PSystem
  (components [_] #{:position})
  (setup [_]
    nil)
  (run [_ globals ents]
    (.render renderer scene camera))
  )

(defn graphics-system
  []
  (map->GraphicsSystem (graphics/setup-threejs)))