(ns game.systems)

(defprotocol PSystem
  "System that acts on entities"
  (components [this] "returns the components this system deals with")
  (setup [this] "called to set up system, for runtime side effect
                 mutable stuff, handle other initialization in
                 constructor")
  (run [this globals entities]
    "called with all entitities that have all of the components required,
     globals is a map with global data about this tick, time, timedelta,
     keypresses etc..."))