(ns car-race.1_orig
  (:require [clojure.string :as str]))

(defn print-state 
  "Prints out the state of the race."
  [info]
  (println "Step" (:step info) "of" (:time info))
  (doseq [c (:cars info)]
    (print (str (str/join (repeat c "-")) \newline))))

(defn rand-inc
  "Maybe increments a value, maybe not."
  [x]
  (if (< (rand) 0.7) (inc x) x))

(defn move-cars
  "Moves a vector of cars"
  [cars]
  (mapv rand-inc cars))

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
