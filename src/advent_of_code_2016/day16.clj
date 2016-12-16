(ns advent-of-code-2016.day16
  (:require [clojure.string :as str]))

(def ^:private input (map #(= \1 %) "11110010111001001"))

(defn- dragon [xs]
  (concat xs [false] (map not (reverse xs))))

(defn- checksum [data]
  (map (fn [[x y]] (= x y)) (partition 2 data)))

(defn- solve [required-size]
  (let [data (->> (iterate dragon input)
                  (drop-while #(< (count %) required-size))
                  (first)
                  (take required-size))
        check (->> (iterate checksum data)
                   (drop-while #(even? (count %)))
                   (first))]
    (apply str (map #(if % \1 \0) check))))

(defn day16-1 [] (solve 272))

(defn day16-2 [] (solve 35651584))