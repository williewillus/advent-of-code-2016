(ns advent-of-code-2016.day3
  (:require [clojure.string :as str]))

(defn- valid-triangle [[a b c]]
  (and (> (+ a b) c) (> (+ a c) b) (> (+ b c) a)))

(defn- to-triple [^String line]
  "Turns line into 3-vector"
  (let [[a b c] (re-seq #"\d+" line)]
    [(Long/parseLong a) (Long/parseLong b) (Long/parseLong c)]))

(defn- to-triples [^String input]
  "Turns each line into a 3-vector, returns seq of those"
  (let [lines (str/split input #"\R+")]
    (map to-triple lines)))

(defn day3-1 [^String input]
  (->> (to-triples input)
       (filter valid-triangle)
       (count)))

(defn- transpose
  [[[a1 b1 c1]
    [a2 b2 c2]
    [a3 b3 c3]]]

  [[a1 a2 a3] [b1 b2 b3] [c1 c2 c3]])

(defn day3-2 [^String input]
  (->> (to-triples input)
       (partition 3)
       (mapcat transpose)
       (filter valid-triangle)
       (count)))