(ns advent-of-code-2016.day2-2
  (:require [clojure.string :as str]))

(def keypad-size 5)
(def fancy-keypad [nil nil 1 nil nil
                   nil  2  3  4  nil
                   5    6  7  8   9
                   nil  \A \B \C nil
                   nil nil \D nil nil])

; all work on 0-based indices into above vector
(defn- row [pos] (quot pos keypad-size))
(defn- col [pos] (mod pos keypad-size))

(defn- up [pos]
  (let [new-pos (- pos keypad-size)]
    (if (or (zero? (row pos)) (nil? (nth fancy-keypad new-pos)))
      pos new-pos)))

(defn- down [pos]
  (let [new-pos (+ pos keypad-size)]
    (if (or (= (row pos) (- keypad-size 1)) (nil? (nth fancy-keypad new-pos)))
      pos new-pos)))

(defn- left [pos]
  (let [new-pos (dec pos)]
    (if (or (zero? (col pos)) (nil? (nth fancy-keypad new-pos)))
      pos new-pos)))

(defn- right [pos]
  (let [new-pos (inc pos)]
    (if (or (= (col pos) (- keypad-size 1)) (nil? (nth fancy-keypad new-pos)))
      pos new-pos)))

(defn- calc-pos [pos op]
  (case op
    \U (up pos)
    \D (down pos)
    \L (left pos)
    \R (right pos)))

(defn- process-line [start-pos ^String line]
  (println (nth fancy-keypad start-pos))
  (reduce calc-pos start-pos line))

(defn day2-2 [^String input]
  (nth fancy-keypad (reduce process-line 10 (str/split-lines input))))

