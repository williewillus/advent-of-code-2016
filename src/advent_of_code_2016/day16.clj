(ns advent-of-code-2016.day16
  (:require [clojure.string :as str])
  (:import (java.util Arrays)))

(def ^:private ^booleans input (boolean-array (map #(= \1 %) "11110010111001001")))

(set! *warn-on-reflection* true)

(defn- dragon [^booleans xs]
  ; Imperative version of: (concat xs false (map not (reverse xs)))
  (let [len (alength xs)
        result (boolean-array (inc (* 2 len)))]
    (System/arraycopy xs 0 result 0 len)
    (aset result len false)
    (doseq [i (range len)]
      (let [reverse-idx (- (dec len) i)
            shifted-reverse-idx (+ (inc len) reverse-idx)]
        (aset result shifted-reverse-idx (not (aget xs i)))))
    result))

(defn- checksum [^booleans xs]
  ; Imperative version of: (map (fn [[x y]] (= x y)) (partition 2 xs))
  (let [len (alength xs)
        result (boolean-array (int (/ len 2)))]
    (doseq [i (range 0 len 2)]
      (aset result (int (/ i 2)) (= (aget xs i) (aget xs (inc i)))))
    result))

(defn- solve [required-size]
  (let [^booleans data (->> (iterate dragon input)
                            (drop-while #(< (alength ^booleans %) required-size))
                            (first))
        truncated-data (Arrays/copyOf data (int required-size))
        csum           (->> (iterate checksum truncated-data)
                            (drop-while #(even? (alength ^booleans %)))
                            (first))]
    (apply str (map #(if % \1 \0) csum))))

(defn day16-1 [] (solve 272))

(defn day16-2 [] (solve 35651584))
