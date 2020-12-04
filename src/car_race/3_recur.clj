(ns car-race.3-recur
  (:require [clojure.string :as str]))

(defn print-step
  "Prints out a step of the race. The arguments to this function are the overall
  time `n`, the number of the current step `i`, and a sequence of integers
  `cars` representing the distance travelled so far by each car in the race."
  [n i cars]
  (println "Step" i "of" n)
  (doseq [c cars]
    (print (str (str/join (repeat c "-")) \newline))))

(defn rand-inc
  "Maybe increments a value, maybe not."
  [x]
  (if (< (rand) 0.7) (inc x) x))

;; We use loop/recur to cycle through the steps of the race, and we don't have
;; a separate function for incrementing the cars, we just use `map` and
;; `rand-inc` directly inside this `race` function.
(defn race [num-cars num-steps]
  (println "Racing" num-cars "cars for" num-steps "steps.")
  (loop [i 0 cars (map rand-inc (repeat num-cars 0))]
    (when (< i num-steps)
      (print-step num-steps (inc i) cars)
      (recur (inc i) (map rand-inc cars)))))

(comment
  (race 3 5))
