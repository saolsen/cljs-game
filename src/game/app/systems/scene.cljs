(ns game.systems.scene
  (:require [game.graphics :as graphics]
            [game.entities :as ent]
            [game.components :as comp])
  (:use [game.systems :only [PSystem]]))

(defrecord SceneSystem [added]
  PSystem
  (components [_] #{})
  (setup [_]
    ;; lets make a floor!
    nil)
  (run [_ globals ents]
    (when-not @added
      (reset! added true)
      (let [floor (graphics/setup-floor)]
        {(ent/gen-id) [(comp/renderable (constantly floor) first)]})))
  )

(defn test-scene-system [] (SceneSystem. (atom false)))