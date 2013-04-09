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
      
      (hash-map
       ;; a few of nate's trees
       (ent/gen-id) [(comp/renderable
                      (partial graphics/tree 200 150 200 75)
                      (fn [old ent] old))
                     (comp/position 500 0 0)]
       (ent/gen-id) [(comp/renderable
                      (partial graphics/tree 60 80 60 25)
                      (fn [old ent] old))
                     (comp/position (- 500) 0 0)]
       ;now
       (ent/gen-id) [(comp/renderable
                      (partial graphics/tree 80 120 100 30)
                      (fn [old ent] old))
                     (comp/position 0 0 500)]
       (ent/gen-id) [(comp/renderable
                      (partial graphics/tree 30 35 25 10)
                      (fn [old ent] old))
                     (comp/position 0 0 (- 500))]

       ;; ;;shitty tree
       ;; (ent/gen-id) [(comp/renderable
       ;;                (partial graphics/tree 500 200 600 50)
       ;;                (fn [old ent] old))
       ;;               (comp/position 0 0 0)]
       
       ;; antisun
       (ent/gen-id) [(comp/renderable
                      graphics/cube
                      (fn [old ent] old))
                     (comp/position 0 250 0)]
       ))
    )
  )

(defn stuff-system [] (StuffSystem. (atom false)))
