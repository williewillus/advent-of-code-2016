(ns advent-of-code-2016.day15
  (:require [clojure.string :as str]))

(defn- parse-input [lines]
  (reduce
    (fn [acc line]
      (let [[idx period start] (map #(Long/parseLong %)
                                      (rest (re-find #"Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+)\." line)))]
        (conj acc #(zero? (mod (+ idx start %) period)))))
    [] lines))

(defn solve [^String input]
  (let [disk-preds (apply every-pred (parse-input (str/split input #"\R+")))]
    (first (filter disk-preds (range)))))

(defn day15-1 [^String input] (solve input))

(defn day15-2 [^String input] (solve (str input "\nDisc #7 has 11 positions; at time=0, it is at position 0.")))
