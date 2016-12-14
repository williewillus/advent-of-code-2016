(ns advent-of-code-2016.day2
  (:require [clojure.string :as str]))

(def ^:private keypad-size 3)

; row/col work on 0-based indices
(defn- row [pos] (quot (dec pos) keypad-size))
(defn- col [pos] (mod (dec pos) keypad-size))

; these work on 1-based indices
(defn- up [pos] (if (zero? (row pos)) pos (- pos keypad-size)))
(defn- down [pos] (if (= (row pos) (- keypad-size 1)) pos (+ pos keypad-size)))
(defn- left [pos] (if (zero? (col pos)) pos (dec pos)))
(defn- right [pos] (if (= (col pos) (- keypad-size 1)) pos (inc pos)))

(defn- calc-pos [pos op]
  (case op
    \U (up pos)
    \D (down pos)
    \L (left pos)
    \R (right pos)))

(defn- process-line [start-pos ^String line]
  (println start-pos)
  (reduce calc-pos start-pos line))

(defn day2-1 [^String input]
  (reduce process-line 5 (str/split input #"\R+")))
