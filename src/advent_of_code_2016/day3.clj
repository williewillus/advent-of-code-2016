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

(defn- transpose [triple-of-triples]
  [(map first triple-of-triples) (map second triple-of-triples) (map #(nth % 2) triple-of-triples)])

(defn day3-2 [^String input]
  (->> (map to-triple (str/split input #"\R+"))
       (partition 3)
       (mapcat transpose)
       (filter valid-triangle)
       (count)))