(ns game.profiling)
;; This is kind of hacky, uses some global state :(
;; Does this because I need to be able to not turn it on and have
;; those functions return nothing (which will hopefully not be much
;; overhead)

;; Use the macro in game.macros.profiling to make this easier.

(def *profiler* nil)

(defprotocol PProfiler
  (start-time [this function-name]
    "records start time of an execution of a function, returns an exectuion id")
  (end-time [this id]
    "finishes the recording of an execution of a function")
  (check-profiles [this]
    "returns string of profiler information"))

(defrecord Profiler [nextid functions open-executions]
  PProfiler
  (start-time [_ function-name]
    (let [time (.now js/Date)
          id (swap! nextid inc)]
      (swap! open-executions assoc id {:name function-name :start time})
      id))
  (end-time [_ id]
    (let [time (.now js/Date)
          {:keys [name start]} (get @open-executions id)
          add-time (fn [m n s e]
                     (let [length (- e s)
                           field (get m n)
                           count (:count field)
                           avg (:avg field)]
                       (assoc m n
                              (if count
                                {:count (inc count)
                                 :avg (/ (+ (* avg count) length) (inc count))}
                                {:count 1 :avg length}))))]
      (swap! functions add-time name start time)
      (swap! open-executions dissoc id)))
  (check-profiles [_] (pr-str @functions))
)

(defn initialize [] (def *profiler* (Profiler. (atom 0) (atom {}) (atom {}))))

(defn start-time! [function] (when *profiler* (start-time *profiler* function)))
(defn stop-time! [function] (when *profiler* (end-time *profiler* function)))

(defn check [] (if-not *profiler*
                 (.log js/console "profiling is not enabled")
                 (.log js/console (check-profiles *profiler*))))
