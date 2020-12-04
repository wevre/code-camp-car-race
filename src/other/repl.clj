(ns other.repl)

;;;; Code Camp Review



;;; Jack-in (C-M-c, C-M-j) and then load this 
;;; file (C-M-c, Enter) in the REPL.
;;; NOTE: In key-stroke notation, C means CMD 
;;; and M means META which is another name for 
;;; the ALT key. We use META because it can 
;;; stand for either ALT on Windows, or OPT 
;;; on Mac. It's an Emacs thing, kinda strange, 
;;; but useful.



;;;
;;; Things that evaluate to themselves
;;; a.k.a. scalars, primitives, literals, ...
;;;

;; Numbers
42
3.14
5/7
(/ 6 8)

;; Strings
"hello"

;; Keywords
:left
:important-thing
:right

;; Characters
\a
\b
\u253C
\newline

;; Other literals
true
false
nil



;;;
;;; Collections, creating them
;;; 

;; Lists
'(1 2 "fred") ; Need the ' in order to treat the list as data.
(quote (1 2 "fred"))
(list "a" :b \c)
(cons 1 '(4 5 6))

;; Vectors
["A" :skip "C"]
(vector 1 2 "three")

;; Maps
{:x 3.2 :y -6.1}
(hash-map :top 4.5 :bottom 0.7)

;; Sets
#{:do :re :mi}
(hash-set :fa :so :la :la)

;; Create one type of collection from another
(vec '(0 10 20))
(set '(:a :b :c))
(into () [1 2 3])
(into '(0 1 2) [3 4 5])
(into {} [[:a 1] [:b 2]])
(into #{} ["tom" "fred" "harry"])
(into [] {:x 1.2 :y 3.4})
; Note maps are treated as a seq of key-value pairs.
; Note `into` uses `conj` under the hood, so lists get added
; at the front, vectors at the end.



;;;
;;; Collections, examining them
;;; 

;; Lists
(first '(1 2 "fred"))
(rest '(1 2 "fred"))
(cons (first '(0 1 2)) (rest '(0 1 2)))

;; Vector
(get [:a :b :c] 1)
([:a :b :c] 1)
(nth [:a :b :c] 1)
(nth [:a :b :c] 5 "missing")
; `first` and `rest` also work on all collections.

;; Map
(get {:car "Honda" :state "UT"} :car)
(get {:car "Honda" :state "UT"} :color "Unknown")
({:car "Honda" [0 1] "position" :state "UT"} [0 1])
(:state {:car "Honda" :state "UT"})
(contains? {:car "Honda" :state "UT"} :car)
(keys {:car "Honda" :state "UT"})
(vals {:car "Honda" :state "UT"})

;; Set
(contains? #{:do :re :mi} :do)
(get #{:do :re :mi} :mi)
(get #{:do :re :mi} :fa "There is no fa")
(#{:do :re :mi} :re)
(:re #{:do :re :mi})

;; Generic collection functions
(count [0 1 2])
(count {:a 1 :b 2 :c 3})
(empty? '(\a \b \c))
(empty? [])
(seq ()) ; Idiomatic to use `seq` to test for 'not-empty'
(seq [0 1])
(every? odd? [1 3 5 7])
(some odd? [0 1 2 3])
(not-every? even? [1 3 5 7])
(not-any? pos? [1 2 3])
;; Sets are handy to test membership
(every? #{:do :re :mi} [:re :mi :fa :do :fa :so :la])



;;;
;;; Collection, 'changing' them
;;; 

;; Lists
(conj '(0 0 0) 5) ; `conj` adds to _front_ of list.

;; Vector
(assoc ["tom" "hal" "eli" "bob"] 3 "rob")
(conj [\a \b \c] \z) ; `conj` adds to _end_ of vector.

;; Map
(assoc {:x 3.3 :y 4.5} :z 2.5)
(dissoc {:name "Fred" :car "Sedan" :pet "Dino"} :pet)
(conj {:a 1} [:b 2]) ; Note how we pass key-value as a pair

;; Set
(conj #{:do :re :mi} :fa)
(disj #{:north :south :east :west} :west)



;;;
;;; Symbols and binding
;;; 

;; To bind a value to a symbol, use `def`.
(def height 15.7)
;; Now `height` can be used anywhere instead of 
;; typing out '15.7' each time.
(+ height 23.3)

(def my-list '(0 1 2 3 4 5))
(first my-list)
(rest my-list)

;; Binding also happens when functions are called.
;; Arguments passed to the function are 'bound' to
;; the function's parameters. More on that later.

;; The `let` form creates temporary, local bindings.
(def x 7)
(let [x 12 y 3]
  (* x y))
x ; `x` was 12 inside the let form, but still 7 outside.



;;; 
;;; Clojure evaluation
;;; 

;; The basic rule of evaluation: the first item in a list
;; is treated as a function or operator, and the rest of the
;; items in the list are arguments to said function/operator.

(count                '(1 2 3))
;^^^^^ the function   ^^^^^^^^ the arguments

;; If we want to treat a list as _data_, and prevent Clojure
;; from evaluating it, we have to 'quote' the list, either
;; with the `quote` function, or with the ' shortcut.

'(count [:a :b :c])
(first *1)
(quote (+ 3 5))
;; What happens when you evaluation this?
(1 2 3) ; Oops! Need the quote.



;;;
;;; Functions
;;;

;; We can create our own functions.
(fn [] (println "Hello world"))

;; If we want to use the function later, we can
;; bind it to a symbol with `def`.
(def greeter (fn [] (println "Hello world")))
(greeter)

;; We can abbreviate the above with `defn`.
(defn bouncer [] (println "Let's see your ID"))
(bouncer)

;; Anonymous functions can be defined with #()
;; (good for simple functions passed to, say, `map`).
#(+ 3 %)
(map #(+ 3 %) [1 2 3])

;; Function parameters go inside the vector after `fn` or after
;; the function name in `defn`. When a function is called, arguments
;; will be bound to these parameters.
(defn do-some-math [x y]
  (println "x + y is" (+ x y))
  (println "x * y is" (* x y)))
(do-some-math 3 5)



;;;
;;; Flow control
;;;

(if (pos? 7) "the number is positive" "not positive")
(when (odd? 3) (println "3 is definitely odd"))
(if-let [x (#{:do :re :me} :fa)]
  (str "We found " x)
  (str "Didn't find anything, result was "))

(let [x (#{:do :re :me} :fa)]
  (if x
    (println "We found" x)
    (println "Didn't find anything")))

;; There is also `when-let`, `if-not`, and `when-not`.

;; `cond` let's us test multiple conditions.
(let [x 37] ; Try changing this value bound to `x`.
  (cond
    (< 50 x) "number is greater than 50"
    (< 25 x) "number is between 25 and 50"
    (< 0 x) "number is between 0 and 25"
    :else "number is 0 or below"))

(let [grade "D"]
  (case grade 
    "A" "Stellar performance"
    "B" "Solid effort"
    "C" "C's get degrees"
    "D" "Hmmm...."
    "F" "Needs improvement"
    "unknown"))



;;;
;;; Destructuring
;;;



;;;
;;; Recursion
;;;
