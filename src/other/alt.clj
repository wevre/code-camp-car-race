(ns other.alt
  "Collects the results of the race into a list, then prints them out."
  (:require [clojure.string :as str]))

;;; This is an alternate version of car-race that produces a sequence of car 
;;; distances, for as many steps as you want your race to last, and then turns
;;; around and prints out the race results. To contrast, the prior version 
;;; produced and printed a single step of the race, and then discarded that and 
;;; moved on to the next step. This version produces all the steps, collecting 
;;; them in a sequence, and then you print the entire sequence of race results 
;;; in one go.

;;; You might want to open the earlier version to the right so you can compare 
;;; them side-by-side.

;;; Jack-in (C-M-c, C-M-j) and then load this file (C-M-c, Enter) in the REPL.
;;; NOTE: In key-stroke notation, C means CMD and M means META which is another
;;; name for the ALT key. We use META because it can stand for either ALT on
;;; Windows, or OPT on Mac. It's an Emacs thing, kinda strange, but useful.

;;; Here is our first function. It is very similar to the earlier version, 
;;; except instead of passing in a map as a single argument, which we called
;;; `info`, and which we have to unpack to get at the values inside--instead we
;;; pass in the three things we need, total time, current step number, and cars,
;;; each as a separate argument.

;;; One other thing. I fixed a bug in our prior version. If a car had distance 
;;; of 0, it should have printed out an empty line, but it was getting skipped.
;;; To fix that, I switched from `println` to `print` and included my own 
;;; explicit `\newline`.

(defn print-step
  "Prints out a step of the race. The arguments to this function are the overall
  time `n`, the number of the current step `i`, and a sequence of integers
  `cars` representing the distance travelled so far by each car in the race."
  [n i cars]
  (println "Step" i "of" n)
  (doseq [c cars]
    (print (str (str/join (repeat c "-")) \newline))))

(comment
  ;; Recall that `doseq` lets us execute an expression for each element of a
  ;; collection, and it's really great for expressions that produce side-effects
  ;; like `print` or `println`. It's sort of like this imaginary conversation:
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
  
  ;; Evaluate this line (M-Enter) to see what it does. Put your cursor in front 
  ;; of different parens and evaluate individual forms with C-Enter. Work your 
  ;; way from the inner forms out to the final outer form.
  (print (str (str/join (repeat 3 "-")) \newline))

  ;; To see what `print-step` produces, try executing this line with M-Enter:
  (print-step 6 3 [3 4 3])

  ;; Change the arguments and see how the output changes:
  (print-step 100 4 [3 0 5 2 7])
  ;; Try this next one a few times. How many 'cars' are there? What is the
  ;; longest distance that will be printed for any one car?
  (print-step 50 3 (take 7 (repeatedly #(rand-int 20))))
  ;; Maybe try some more of your own. Experiment.
  )

;;; Our second function is good old `rand-inc`. No changes.

(defn rand-inc 
  "Maybe increments its input, maybe not."
  [x] (if (< (rand) 0.7) (inc x) x))

;;; Here is our race function. It producing a lazy, infinite sequence of cars, 
;;; where each subsequent element is randomly incremented from its predecessor.

(defn race
  "Returns a lazy (infinite!) sequence of randomly incremented cars."
  [cars]
  (lazy-seq
   (cons cars (race (map rand-inc cars)))))

(defn race* [num-cars]
  (letfn [(step ([cars] (lazy-seq (cons cars (step (map rand-inc cars))))))]
    (step (repeat num-cars 0))))

(comment
  ;; To be able to grapple with an infinte sequence (how do you print something 
  ;; like that out?) we use the `take` function, to give us just the first few 
  ;; elements of a sequence.
  
  ;; To see what the `race` function does, try executing this line:
  (take 5 (race '(0 0 0)))
  ;; How many 'steps' of the race are we producing? How many cars are we racing?
  ;; What about this call? How many steps and how many cars?
  (take 3 (race '(0 0 0 0 0)))
  ;; Can you run a 100-step race? What would you change?
  
  ;; Take a look at the `race` function and in particular that `cons` function.
  ;; Go look up `cons` and `lazy-seq` at clojuredocs.org to find out more about 
  ;; them. If we had a conversation with `cons` it would look like this:
  ;;   <cons>: I'm going to build you a sequence, what is the first element of
  ;;           your sequence?
  ;;   <me>: It's these 'cars'.
  ;;   <cons>: Great, now what is the rest of your sequence going to be?
  ;;   <me>: It's going to be some randomly updated cars.
  ;;   <cons>: Come again?
  ;;   <me>: I need to randomly update the cars, I'll use `rand-inc` to do that.
  ;;   <cons>: So far so good.
  ;;   <me>: And then I'll pass those randomly update cars to `race`. Whatever
  ;;         comes back from `race`, that is the rest of my sequence.
  ;;   <cons>: Okay. I'll get on it.
  
  ;; The good news is that even though `race` will produce an infinite sequence,
  ;; it doesn't do that right away. The `cons` function keeps track of the first
  ;; element (`cars`) and the _instructions_ for how to generate the rest of the
  ;; sequence, but it doesn't execute those _instructions_ until needed. By
  ;; using something like `(take 4 (race ...))` we limit things so only 4 items
  ;; will be produced. Most of the sequence functions, `map`, `filter`, `take`,
  ;; `drop`, etc. take and return lazy sequences.
  
  ;; Evaluate each of these lines to set up (i.e. define) some values we'll be
  ;; using. Here's a trick. Use M-Enter to evaluate the entire form and define 
  ;; the value, then put your cursor next to (or inside) a symbol name (like 
  ;; 'num-cars') and hit C-Enter to see the value of `num-cars`.
  (def num-cars 3)
  (def num-steps 5)
  (def starting-cars (repeat num-cars 0))
  (def results (take num-steps (race starting-cars)))
  
  ;; We can use `doseq` and start building something that will, eventually, 
  ;; print out our nice race results. Let's start with something bare-bones:
  (doseq [step results] (println step))
  ;; Evaluate that line above and you'll see that it is our race results, the 
  ;; raw data, but it isn't our pretty lines and labels like "Step 3 of 5". 
  ;; Let's try and use our `print-step` function defined above. Recall that it
  ;; takes 3 arguments: the total number of steps, the number of the current 
  ;; step, and the cars. We have some of that info ready to go.
  (doseq [step results] (print-step num-steps ___ step))
  ;; We're just missing the middle argument, the number of the current step. 
  ;; Where do we get that from? We could plug in a dummy value, just to be able 
  ;; to print _something_ even though it is wrong. Evaluate this next line.
  (doseq [step results] (print-step num-steps -1 step))
  ;; Notice that the labels are wrong, they each say "Step -1 of 5".
  
  ;; We need our results to be numbered. Instead of this:
  ;;   (0 0 0)   <- each one of these...
  ;;   (1 0 1)   <- ...takes a turns as `step`...
  ;;   (2 1 1)   <- ...inside `doseq`
  ;;   ...
  ;; as our results that pass through `doseq`, we want something like this:
  ;;   [0 (0 0 0)]   <- we want each of these...
  ;;   [1 (1 0 1)]   <- ...to be `step`...
  ;;   [2 (2 1 1)]   <- ...inside `doseq`
  ;;   ...
  ;; where our cars are bundled up with a counter. If this was what we
  ;; passed through `doseq`, then our expression inside `doseq` could be this:
  (print-step num-steps (first step) (second step))
  ;; Note how `(first step)` would pull out the step number, and `(second step)`
  ;; would pull out the cars. You can see this in action. Evaluate each line:
  (def step [0 '(0 0 0)])
  step
  (first step)
  (second step)
  
  ;; So how do we do this? We want our results transformed from the list of raw
  ;; cars, to cars with a counter attached. Each result will be a vector where
  ;; the first item of the vector is a counter, and the second item is the 
  ;; original raw cars. We want to 'map' some function over our results that 
  ;; will transform them.
  (map some-func results)
  ;; So what should some-func be? We could use `vector`
  (map vector results)
  ;; If you evaluate that, it does wrap our cars inside a vector, but we're
  ;; missing the first item, the counter, for each one.
  
  ;; Let's step back and review what map does. This kind of expression
  (map f coll)
  ;; maps the function `f` over each element of the collection `coll`. The 
  ;; function `f` takes one argument, because it sees the elements of `coll` one
  ;; at a time. But `map` will also work with multiple collections:
  (map f coll1 coll2)
  ;; Now, the function `f` has to take _two_ arguments, one from `coll1` and one
  ;; from `coll2`. The result will be something that combines the first element
  ;; from `coll1` with the first element from `coll2`, then the second element 
  ;; of `coll1` will be combined with the second element of `coll2` and so on
  ;; until one of them runs out of elements. Then `map` will stop.
  
  ;; Try this out to get a feel for it:
  (map str '("Mike" "Jordan" "Jared") '("Weaver" "Bleak" "Cahoon"))
  ;; Or this one:
  (map + [0 1 2] [100 200 300])
  
  ;; Back to our results, since the `vector` function will happily take more
  ;; than one argument, we can still use it, we just need an additional 
  ;; collection to give us our counters, 0, 1, 2, etc. How about this:
  (map vector (range) results)
  ;; Evaluate that and you'll see it produces numbered results just like we
  ;; wanted. Another name for that is we've 'indexed' our collection. This is
  ;; so common there is a built-in function (of course there is!) for doing it.
  (map-indexed vector results)
  ;; Evalate that and compare. Just what we want, right?
  
  ;; Now we are ready. Let's get some numbered results:
  (def numbered-results (map-indexed vector results))
  ;; And process those with `doseq`.
  (doseq [step numbered-results]
    (print-step num-steps (first step) (second step)))
  
  ;; Let's repeat what we just did so we can see all the steps together (the
  ;; linter won't like us because we are re-def'ing the same symbol, but just
  ;; ignore that):
  (def num-cars 3)
  (def num-steps 5)
  (def starting-cars (repeat num-cars 0))
  (def results (take num-steps (race starting-cars)))
  (def numbered-results (map-indexed vector results))
  (doseq [step numbered-results]
    (print-step num-steps (first step) (second step)))
  
  ;; This works, but let's clean it up and make a few tweaks.
  ;; (1) Let's wrap this in a function.
  (defn main1 []
    (let [num-cars 3
          num-steps 5
          starting-cars (repeat num-cars 0)
          results (take num-steps (race starting-cars))
          numbered-results (map-indexed vector results)]
      (doseq [step numbered-results]
        (print-step num-steps (first step) (second step)))))
  
  ;;     Now you can call it
  (main1)
  
  ;; (2) We are creating a lot of extra symbols, let bindings, that we don't
  ;;     really need. We could use the ->> threading macro to clean up stuff.
  (defn main2 []
    (let [num-cars 3
          num-steps 5
          starting-cars (repeat num-cars 0)
          results (->> (race starting-cars)
                       (take num-steps)
                       (map-indexed vector))]
      (doseq [step results]
        (print-step num-steps (first step) (second step)))))
  
  (main2)

  ;; (3) If you evaluate and then call `main2`, notice that the first step it 
  ;;     prints is "Step 0 of 5" and the cars haven't moved at all, and it stops
  ;;     at "Step 4 of 5". Oops. We need to skip the first step, the first set 
  ;;     of results. Let's use the `drop` function for that.
  (defn main3 []
    (let [num-cars 3
          num-steps 5
          starting-cars (repeat num-cars 0)
          results (->> (race starting-cars)
                       (drop 1)   ; skip the first set of results
                       (take num-steps)
                       (map-indexed vector))]
      (doseq [step results]
        (print-step num-steps (first step) (second step)))))

  (main3)
  ;; (4) How does that look? Our cars are moving, and we're no longer printing
  ;;     those 'zero' results before the race has started, but our steps are 
  ;;     still numbered wrong. Can you see why? We need to change the order of 
  ;;     things inside that threading macro.
  (defn main4 []
    (let [num-cars 3
          num-steps 5
          starting-cars (repeat num-cars 0)
          results (->> (race starting-cars)   ; produce results
                       (map-indexed vector)   ; number them
                       (drop 1)               ; skip the first one
                       (take num-steps))]     ; limit to `num-steps`
      (doseq [step results]
        (print-step num-steps (first step) (second step)))))

  (main4)
  )

;;; Here is our final `-main` function, same as `main4` above.

(defn -main []
  (let [num-cars 3
        num-steps 5
        starting-cars (repeat num-cars 0)
        results (->> (race starting-cars)   ; produce results
                     (map-indexed vector)   ; number them
                     (drop 1)               ; skip the first one
                     (take num-steps))]     ; limit to `num-steps`
    (println "Racing" num-cars "cars for" num-steps "steps.")
    (doseq [step results] ;;<- do some destructuring here
      (print-step num-steps (first step) (second step)))))

(comment
  ;; You can execute it in the REPL by evaluating this line,
  (-main)
  ;; and you can also do it from the command line with
  ;;    clojure -m car-race.alt
  ;; or something similar on windows.
 
  (defn do-race [num-cars num-steps]
    (let [starting-cars (repeat num-cars 0)
          results (->> (race starting-cars)   ; seq of results
                       (map-indexed vector)   ; number them
                       (drop 1)               ; skip the first one
                       (take num-steps))]     ; limit to `num-steps`
      (println "Racing" num-cars "cars for" num-steps "steps.")
      (doseq [step results] ;;<- do some destructuring here
        (print-step num-steps (first step) (second step)))))
  
  (defn do-race [num-cars num-steps]
    (println "Racing" num-cars "cars for" num-steps "steps.")
    (loop [i 0 results (drop 1 (race* num-cars) #_(race (repeat num-cars 0)))]
      (when (< i num-steps)
        (print-step num-steps (inc i) (first results))
        (recur (inc i) (rest results)))))
  (do-race 3 5)
  
  (defn do-race [num-cars num-steps]
    (let [results (->> (repeat num-cars 0)
                       (iterate #(map rand-inc %))
                       (map-indexed vector)
                       (drop 1)
                       (take num-steps))]
      (doseq [step results]
        (print-step num-steps (first step) (second step)))))
  
  ;; NOTE: can drop the `race` function entirely and just
  ;; (map rand-inc cars) each time we need to, instead of 
  ;; worrying about a sequence.
  
  )
