(ns advent-of-code-2016.day15
  (:require [clojure.string :as str]))

(defn- parse-input [lines]
  (reduce
    (fn [acc line]
      (let [[[_ index pos init]] (re-seq #"Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+)\." line)
            idx (Long/parseLong index)
            start (Long/parseLong init)
            period (Long/parseLong pos)]
        (conj acc #(zero? (mod (+ idx start %) period)))))
    [] lines))

(defn day15 [^String input]
  (let [disk-preds (apply every-pred (parse-input (str/split input #"\R+")))]
    (some #(when (disk-preds %) %) (range))))