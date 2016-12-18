(ns advent-of-code-2016.day18
  (:require [clojure.string :as str]))

(set! *warn-on-reflection* true)

(defn- compute-next [^booleans prev]
  (amap prev idx ret
        (let [left (if (zero? idx)
                     false (aget prev (dec idx)))
              right (if (= (dec (alength prev)) idx)
                      false (aget prev (inc idx)))]
          (not= left right)))) ; the cases given boil down to just this check

(defn- solve [^String input num-rows]
  (let [init-line (boolean-array (map #(case % \. false \^ true) (first (str/split input #"\R+"))))
        lines (take num-rows (iterate compute-next init-line))]
    (reduce + (map #(count (filter false? %)) lines))))

(defn day18-1 [^String input] (solve input 40))

(defn day18-2 [^String input] (solve input 400000))
