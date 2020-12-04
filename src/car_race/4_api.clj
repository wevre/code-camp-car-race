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

;; Instead of our own function to produce an infinite sequence of cars, we use
;; the built-in function `iterate`.
(defn race [num-cars num-steps]
  (let [results (->> (repeat num-cars 0)
                     (iterate #(map rand-inc %))
                     (map-indexed vector)
                     (drop 1)
                     (take num-steps))]
    (doseq [[i cars] results]
      (print-step num-steps i cars))))

(comment
  (race 3 5))