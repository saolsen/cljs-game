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

(defn renderable
  "setup must return a threejs object ;;TODO multiple ones
   that can be added to the scene
   update must be a function of that object and the entity"
  [setup update]
  {:setup setup :update update :type :renderable})

(defn player
  "entitiy that represents the player"
  []
  {:type :player})

(defn camera
  "Coordinates for the camera. Vectors for where it's looking and it's position"
  [pitch-object yaw-object]
  {:type :camera
   :pitch-object pitch-object
   :yaw-object yaw-object})

(defn keypresses
  "Keys pressed"
  [down presses]
  {:type :keypresses
   :down down
   :pressed presses})
