(ns advent-of-code-2016.day3
  (:require [clojure.string :as str]))

(defn- valid-triangle [[a b c]]
  (and (> (+ a b) c) (> (+ a c) b) (> (+ b c) a)))

(defn- to-triple [^String line]
  (take 3 (map #(Long/parseLong %) (re-seq #"\d+" line))))

(defn day3-1 [^String input]
  (->> (map to-triple (str/split input #"\R+"))
       (filter valid-triangle)
       (count)))

(defn- transpose
  [[[a1 b1 c1]
    [a2 b2 c2]
    [a3 b3 c3]]]

  [[a1 a2 a3] [b1 b2 b3] [c1 c2 c3]])

(defn day3-2 [^String input]
  (->> (map to-triple (str/split input #"\R+"))
       (partition 3)
       (mapcat transpose)
       (filter valid-triangle)
       (count)))