(ns game.components)

(defn position
  [x y z]
  {:x x :y y :z z :type :position})

(defn velocity
  [x y z]
  {:x x :y y :z z :type :velocity})

(defn acceleration
  "fx fy fz is are functions of time, they return a number that gets added to
   the current velocity"
  [fx fy fz]
  {:fx fx :fy fy :fz fz :type :acceleration})
