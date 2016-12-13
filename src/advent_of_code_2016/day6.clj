(ns advent-of-code-2016.day6
  (:require [clojure.string :as str]))

(def ^:private input-len 8)

(defn- max-val-key [m] (key (first (sort-by val #(compare %2 %1) m))))
(defn- min-val-key [m] (key (first (sort-by val m))))

(defn- solve [input key-extractor]
  (let [lines (str/split-lines input)]
    (doseq [n (range input-len)]
      (println (key-extractor (frequencies (map #(nth % n) lines)))))))

(defn day6-1 [input] (solve input max-val-key))
(defn day6-2 [input] (solve input min-val-key))
