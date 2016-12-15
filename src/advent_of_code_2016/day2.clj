(ns advent-of-code-2016.day2-2
  (:require [clojure.string :as str]))

(def ^:private fancy-keypad
  [nil nil 1 nil nil
   nil  2  3  4  nil
   5    6  7  8   9
   nil  \A \B \C nil
   nil nil \D nil nil])

(def ^:private basic-keypad
  [1 2 3
   4 5 6
   7 8 9])

(defn- keypad-size [kp] (int (Math/sqrt (count kp))))

(defn- row [kp pos] (quot pos (keypad-size kp)))

(defn- col [kp pos] (mod pos (keypad-size kp)))

(defn- up [kp pos]
  (let [new-pos (- pos (keypad-size kp))]
    (if (or (zero? (row kp pos))
            (nil? (nth kp new-pos)))
      pos new-pos)))

(defn- down [kp pos]
  (let [new-pos (+ pos (keypad-size kp))]
    (if (or (= (row kp pos) (dec (keypad-size kp)))
            (nil? (nth kp new-pos)))
      pos new-pos)))

(defn- left [kp pos]
  (let [new-pos (dec pos)]
    (if (or (zero? (col kp pos))
            (nil? (nth kp new-pos)))
      pos new-pos)))

(defn- right [kp pos]
  (let [new-pos (inc pos)]
    (if (or (= (col kp pos) (dec (keypad-size kp)))
            (nil? (nth kp new-pos)))
      pos new-pos)))

(defn- calc-pos [kp pos op]
  (case op
    \U (up kp pos)
    \D (down kp pos)
    \L (left kp pos)
    \R (right kp pos)))

(defn- process-line [kp start-pos ^String line]
  (reduce (partial calc-pos kp) start-pos line))

(defn- solve [kp start-idx input]
  (map (partial nth kp)
       (rest (reductions (partial process-line kp) start-idx (str/split input #"\R+")))))

(defn day2-1 [^String input] (solve basic-keypad 4 input))

(defn day2-2 [^String input] (solve fancy-keypad 10 input))

