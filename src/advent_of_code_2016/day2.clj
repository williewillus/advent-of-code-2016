(ns advent-of-code-2016.day2
  (:require [clojure.string :as str]))

(def keypad-size 3)

(defn row [pos] (quot (dec pos) keypad-size))
(defn col [pos] (mod (dec pos) keypad-size))

(defn up [pos] (if (zero? (row pos)) pos (- pos keypad-size)))
(defn down [pos] (if (= (row pos) (- keypad-size 1)) pos (+ pos keypad-size)))
(defn left [pos] (if (zero? (col pos)) pos (dec pos)))
(defn right [pos] (if (= (col pos) (- keypad-size 1)) pos (inc pos)))

(defn calc-pos [op pos]
  (condp = op
    \U (up pos)
    \D (down pos)
    \L (left pos)
    \R (right pos)))

(defn process-line [^String line start-pos]
  ; todo we repeat this pattern below, is there a more compact way?
  (loop [[c & xs] line
         pos start-pos]
    (if (seq xs) (recur xs (calc-pos c pos)) (calc-pos c pos))))

(defn day2-1 [^String input]
  (loop [[line & xs] (str/split-lines input)
         pos 5]
    (println pos)
    (if (seq xs) (recur xs (process-line line pos)) (process-line line pos))))