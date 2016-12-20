(ns advent-of-code-2016.day20
  (:require [clojure.string :as str]))

(defn- line->pred [line]
  (let [[_ m1 m2] (re-find #"(\d+)-(\d+)" line)
        min (Long/parseLong m1)
        max (Long/parseLong m2)]
    (fn [x] (or (< x min) (> x max)))))

(defn day20-1 [^String input]
  (let [pred (apply every-pred (map line->pred (str/split input #"\R+")))]
    (->> (range)
         (filter pred)
         (first))))
