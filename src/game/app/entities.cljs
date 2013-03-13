(ns game.entities
  (:require [clojure.set :as set]))

;; maybe not the best solution
;; this really stateful and bad
(def last-id (atom 0))

(defn gen-id
  []
  "generates a new entity id"
  (keyword (swap! last-id inc))
  )

(defn get-types
  "gets all the types of the components"
  [components]
  (set (map :type components)))

(defn get-with-components
  "gets the entities with all the needed components"
  [entity-map components]
  (into {} (for [[k v] entity-map
                 :when (set/subset? components (get-types v))]
             [k v])))

(defn change-entities
  "replaces the changed entities"
  [entity-map new-entities]
  (merge entity-map new-entities))

(defn get-component
  "gets the component from an entity's component array"
  [entity-components component-type]
  (first (filter #(= (:type %) component-type) entity-components)))

(defn remove-component
  "returns the entity component array without the one of type component-type"
  [entity-components component-type]
  (filter #(not= (:type %) component-type) entity-components))
