(ns game.systems.stuff
  (:require [game.entities :as ent]
            [game.components :as comp]
            [game.graphics :as graphics])
  (:use [game.systems :only [PSystem]]
        [game.utils :only [log]]))

;; System for adding stuff to the scene willy nilly
(defrecord StuffSystem [setup?]
  PSystem
  (components [_] #{})
  (setup [_] nil)
  (run [_ globals ents]

    (when-not @setup?
      (reset! setup? true)

      ;; make a cube
      ;; {(ent/gen-id) [(comp/renderable graphics/cube
      ;;                                 (fn [old ent] old))
      ;;                (comp/position 0 0 0)]})

      ;; make nate's tree!
       (hash-map 
       (ent/gen-id) [(comp/renderable
                      graphics/tree
                      (fn [old ent] old))
                     (comp/position 0 0 0)]
       ;; antisun
       (ent/gen-id) [(comp/renderable
                      graphics/cube
                      (fn [old ent] old))
                     (comp/position 0 150 0)]
       ))

      )
    )

(defn stuff-system [] (StuffSystem. (atom false)))
