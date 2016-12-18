(ns advent-of-code-2016.day18
  (:require [clojure.string :as str]))

(defn- compute-next [prev]
  (into []
    (map-indexed
      (fn [idx _]
        (let [left (if (zero? idx)
                     false (nth prev (dec idx)))
              right (if (= (dec (count prev)) idx)
                      false (nth prev (inc idx)))]
          (not= left right))) ; the cases given boil down to just this check
      prev)))

(defn- solve [^String input num-rows]
  (let [init-line (mapv #(case % \. false \^ true) (first (str/split input #"\R+")))
        lines (take num-rows (iterate compute-next init-line))]
    (reduce + (map #(count (filter false? %)) lines))))

(defn day18-1 [^String input] (solve input 40))

(defn day18-2 [^String input] (solve input 400000))
