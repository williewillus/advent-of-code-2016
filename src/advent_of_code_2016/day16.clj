(ns advent-of-code-2016.day16
  (:require [clojure.string :as str]))

(def ^:private input "11110010111001001")

(defn- dragon [s]
  (apply str s \0 (map #(if (= \0 %) \1 \0) (str/reverse s))))

(defn- checksum [s]
  (apply str (map (fn [[x y]] (if (= x y) \1 \0)) (partition 2 s))))

(defn- solve [required-size]
  (let [data (subs (->> (iterate dragon input)
                        (remove #(< (count %) required-size))
                        (first))
                   0 required-size)
        check (->> (iterate checksum data)
                   (remove #(even? (count %)))
                   (first))]
    check))

(defn day16-1 [] (solve 272))

(defn day16-2 [] (solve 35651584))