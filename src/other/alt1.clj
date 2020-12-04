(ns other.alt1
  (:require [clojure.string :as str]))

;;;
;;; This is an alternate version of car-race that uses loop/recur to run the
;;; race. Contrast that with the original version which uses recursion (i.e. a
;;; function calling itself). Loop/recur is similar to recursion, as we'll see.
;;;
;;; You might want to open the earlier version to the right so you can compare
;;; them side-by-side.
;;;
;;; Jack-in (C-M-c, C-M-j) and then load this file (C-M-c, Enter) in the REPL.
;;; NOTE: In key-stroke notation, C means CMD and M means META which is another
;;; name for the ALT key. We use META because it can stand for either ALT on
;;; Windows, or OPT on Mac. It's an Emacs thing, kinda strange, but useful.
;;;
;;; Here is our first function. It is very similar to the earlier version,
;;; except instead of passing in a map as a single argument, which we called
;;; `info`, and which we have to unpack to get at the values inside--instead we
;;; pass in the three things we need, total time, current step number, and cars,
;;; each as a separate argument.
;;;
;;; One other thing. I fixed a bug in our prior version. If a car had distance
;;; of 0, it should have printed out an empty line, but it was getting skipped.
;;; To fix that, I switched from `println` to `print` and included my own
;;; explicit `\newline`.
;;;

(defn print-step
  "Prints out a step of the race. The arguments to this function are the overall
  time `n`, the number of the current step `i`, and a sequence of integers
  `cars` representing the distance travelled so far by each car in the race."
  [n i cars]
  (println "Step" i "of" n)
  (doseq [c cars]
    (print (str (str/join (repeat c "-")) \newline))))

(comment
  ;;
  ;; Recall that `doseq` lets us execute an expression for each element of a
  ;; collection, and it's really great for expressions that produce side-effects
  ;; like `print` or `println`. It's sort of like this imaginary conversation:
  ;;
  ;;   <doseq>: What is your collection?
  ;;   <me>: I have this list 'cars'.
  ;;   <doseq>: That's nice. What symbol do you want to use to represent a
  ;;            single element of 'cars'?
  ;;   <me>: Can I use 'c'?
  ;;   <doseq>: Sure you can. No problem. Okay, almost done here, what
  ;;            expression do you want to evaluate for each element in 'cars'?
  ;;            And don't forget, since you chose 'c' as your symbol, you can
  ;;            use 'c' inside your expression if you want to.
  ;;   <me>: Great. Thanks. I want to evaluate
  ;;         '(print (str (str/join (repeat c "-")) \newline))'
  ;;   <doseq>: I'll get right on that.
  ;;   <me>: Thanks.
  ;;   <doseq>: My pleasure.
  ;;
  ;; Evaluate this line (M-Enter) to see what it does. Then put your cursor in 
  ;; front of different parens and evaluate individual forms with C-Enter. Work 
  ;; your way from the inner forms out to the final outer form.
  ;;
  (print (str (str/join (repeat 3 "-")) \newline))
  ;;
  ;; To see what `print-step` produces, try executing this line with M-Enter:
  ;;
  (print-step 6 3 [3 4 3])
  ;;
  ;; Change the arguments and see how the output changes:
  ;;
  (print-step 100 4 [3 0 5 2 7])
  ;;
  ;; Try this next one a few times. How many 'cars' are there? What is the
  ;; longest distance that will be printed for any one car? Hint: look up
  ;; unfamiliar functions at clojuredocs.org, or hover your mouse over and read
  ;; the tooltip text.
  ;;
  (print-step 50 3 (repeatedly 7 #(rand-int 20)))
  ;;
  ;; Maybe try some more of your own. Experiment.
  ;;
  )

;;;
;;; Our second function is good old `rand-inc`. No changes.
;;;

(defn rand-inc
  "Maybe increments its input, maybe not."
  [x] (if (< (rand) 0.7) (inc x) x))

;;;
;;; Here is our race function. We tell it how many cars, and how many steps and
;;; off it goes. Instead of maintaining a map that models the race, it keeps 
;;; track of a counter `i` and a separate list of `cars`, randomly incrementing 
;;; those for each step of the race.
;;;

(defn race
  "Prints out a car race."
  [num-cars num-steps]
  (println "Racing" num-cars "cars for" num-steps "steps.")
  (loop [i 0 cars (map rand-inc (repeat num-cars 0))]   ; <A>
    #_(println "i is" i "and cars is" cars)
    (when (< i num-steps)
      (print-step num-steps (inc i) cars)
      (recur (inc i) (map rand-inc cars)))))            ; <B>

(comment
  ;;
  ;; Let's focus on the lines labeled <A> and <B> above. The lines in between
  ;; should be familiar. So what's going on with `loop` and `recur`?
  ;; First, `loop` sets up bindings (similar to `let`) and acts as a target
  ;; where we can jump back and start over again.
  ;;
  ;; On line <A> we are setting some initial values: `i` starts as 0, and `cars`
  ;; starts as a list of zeros that have been randomly incremented. Even though 
  ;; we are going to loop back to this point, these initial values are only set 
  ;; up one time.
  ;;
  ;; We use the expression below to initialize `cars`. As you did above, you can
  ;; evaluate this line with M-Enter, but also evaluate the inner `repeat` form
  ;; with C-Enter and see how the final value is built up from the inside
  ;; pieces. Change `num-cars` to something else evaluate again.
  ;;
  (let [num-cars 3]
    (map rand-inc (repeat num-cars 0)))
  ;;
  ;; Let's go back to `loop` and `recur`. Inside the `loop` we test if there is 
  ;; time left and print out the current step of the race, similar to the prior 
  ;; version. Then we are ready to loop back and do the next step of the race, 
  ;; which we do with `recur`.
  ;;
  ;; `recur` is a special form. It rebinds `i` and `cars` and repeats the
  ;; body of `loop` with those new values of `i` and `cars`. The order and
  ;; number of arguments in `recur` match up with the bindings established
  ;; in `loop`.
  ;;
  ;;    binding :: initial value (set in `loop`)      :: next value (`recur`)
  ;;    --------::------------------------------------::---------------------
  ;;    i       :: 0                                  :: (inc i)
  ;;    cars    :: (map rand-inc (repeat num-cars 0)) :: (map rand-inc cars)
  ;;
  ;; Try this: uncomment that `println` form just inside `loop`, re-evaluate the
  ;; function (M-Enter) and then run the function to see how `i` and `cars` 
  ;; change with each iteration of loop/recur.
  ;; 
  )

;;; 
;;; Here is our final `-main` function.
;;; 

(defn -main []
  (race 3 5))

(comment
  ;; 
  ;; You can execute `-main` in the REPL by evaluating this line
  ;; 
  (-main)
  ;; 
  ;; and you can also do it from the terminal command line with
  ;;    clojure -m car-race.alt
  ;; or something similar on windows.
  ;; 
  )
