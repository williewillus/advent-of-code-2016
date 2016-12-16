(ns advent-of-code-2016.day16
  (:require [clojure.string :as str]))

(def ^:private input (map #(= \1 %) "11110010111001001"))

(defn- dragon [xs]
  (concat xs [false] (map not (reverse xs))))

(defn- checksum [data]
  (map (fn [[x y]] (= x y)) (partition 2 data)))

(defn- truncate-dragon [input required-size]
  (let [data (first (drop-while #(< (count %) required-size)
                                (iterate dragon input)))]
    (println "got data")
    (take required-size data)))

(defn- solve [required-size]
  (let [data (truncate-dragon input required-size)
        check (first (drop-while #(even? (count %))
                                 (iterate checksum data)))]
    (println "checksum:" (apply str (map #(if % \1 \0) check)))))

(defn- day16-1 [] (solve 272))

(defn- day16-2 [] (solve 35651584))