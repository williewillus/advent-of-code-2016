(ns advent-of-code-2016.day3
  (:require [clojure.string :as str]))

(defn valid-triangle [[a b c]]
  (and (> (+ a b) c) (> (+ a c) b) (> (+ b c) a)))

(defn to-triple [^String line]
  (let [[a b c] (re-seq #"\d+" line)]
    [(Long/parseLong a) (Long/parseLong b) (Long/parseLong c)]))

(defn to-triples [^String input]
  (let [lines (str/split input #"\R+")]
    (map to-triple lines)))

(defn day3-1 [^String input]
    (count (filter valid-triangle (to-triples input))))