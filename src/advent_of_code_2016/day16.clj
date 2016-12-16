(ns advent-of-code-2016.day16
  (:require [clojure.string :as str]))

(def ^:private input "11110010111001001")

(defn- dragon [in required-size]
  (loop [s in]
    (if (< (count s) required-size)
      (recur (apply str s "0" (map #(if (= \0 %) \1 \0) (reverse s))))
      (subs s 0 required-size))))

(defn- checksum [in]
  (loop [csum in]
    (if (even? (count csum))
      (recur (apply str (map (fn [[x y]] (if (= x y) \1 \0)) (partition 2 csum))))
      csum)))

(defn day16-1 [] (checksum (dragon input 272)))

(defn day16-2 [] (checksum (dragon input 35651584)))