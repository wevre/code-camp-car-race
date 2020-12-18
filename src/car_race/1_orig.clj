(ns car-race.1_orig
  (:require [clojure.string :as str]))

(defn print-state 
  "Prints out the state of the race."
  [{:keys [time step cars]}]
  (println "Step" step "of" time)
  (doseq [c cars]
    (print (str (str/join (repeat c "-")) \newline))))

(defn rand-inc
  "Maybe increments a value, maybe not."
  [x]
  (if (< (rand) 0.7) (inc x) x))

(comment
  (rand-inc 10)
  )

(defn move-cars
  "Moves a vector of cars"
  [cars]
  (mapv rand-inc cars))

(comment
  (move-cars [1 2 3])
  )

(defn race
  "Do one step of the race while there is time."
  [state]
  (when (< (:step state) (:time state))
    (let [new-state (-> state
                        (update :step inc)
                        (update :cars move-cars))]
      (print-state new-state)
      (race new-state))))

(def starting-state
  {:time 5
   :step 0
   :cars [0 0 0]})

(comment
  (race starting-state)
  )
