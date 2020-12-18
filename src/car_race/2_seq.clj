(ns car-race.2-seq
  (:require [clojure.string :as str]))

;; This version of print-step is very similar to our original, but instead of
;; passing a map of info, where we broke out :step and :time and :cars, we pass
;; those elements as individual arguments. Also, I fixed a bug to make sure that
;; even if a car had 0 distance, a line for it would still be printed.
(defn print-step
  "Prints out a step of the race. The arguments to this function are the overall
  time `n`, the number of the current step `i`, and a sequence of integers
  `cars` representing the distance travelled so far by each car in the race."
  [n i cars]
  (println "Step" i "of" n)
  (doseq [c cars]
    (print (str (str/join (repeat c "-")) \newline))))

;; Same `rand-inc` as before. This is quite a handy function.
(defn rand-inc
  "Maybe increments a value, maybe not."
  [x]
  (if (< (rand) 0.7) (inc x) x))

;; This function returns a sequence of randomly-incremented groups of cars.
;; Like this: ((0 0 0) (0 1 1) (1 1 2) ...)
;; It is an infinite (!) sequence, so we probably need to limit what we print
;; with the `take` function. See the comment below.
(defn random-cars
  "Returns a lazy (infinite!) sequence of randomly incremented cars."
  [cars]
  (lazy-seq
   (cons cars (random-cars (map rand-inc cars)))))

(comment
  (take 5 (random-cars '(0 0 0 0 0))))

(defn race 
  "Call with number of cars to race, and number of steps in the race, and off it
   goes!"
  [num-cars num-steps]
  (let [results (->> (repeat num-cars 0)    ; starting cars
                     (random-cars)          ; seq of results
                     (map-indexed vector)   ; number them
                     (drop 1)               ; skip the first one
                     (take num-steps))]     ; limit to `num-steps`
    (println "Racing" num-cars "cars for" num-steps "steps.")
    (doseq [[i cars] results] ;;<- do some destructuring here
      (print-step num-steps i cars))))

(comment
  (repeat 5 0)
  (take 5 (random-cars (repeat 5 0)))
  
  (map-indexed vector '((0 0 0) (1 1 1) (2 1 2) (3 2 2)))
  
  (map vector (range) '((0 0 0) (1 1 1) (2 1 2) (3 2 2)) (range))
  
  (map #(vector %1 %2) '("a" "b" "c" "d") '((0 0 0) (1 1 1) (2 1 2) (3 2 2)))
  
  (race 3 5)
  (race 7 200)
  )
