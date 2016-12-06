(ns advent-of-code-2016.day4
  (:require [clojure.string :as str]))

; Clears cruft out of the line and converts types
(defn to-info [^String line]
  (let [[ln name sector hash] (re-find #"([a-z\-]+)-(\d+)\[([a-z]+)\]" line)]
    [(str/replace name "-" "") (Long/parseLong sector) hash]))

(defn valid
  [[name sector hash]]
  (let [freq (frequencies name)
        sort-letters (sort-by (juxt freq identity) #(compare %2 %1) name)] ; sort first by frequency then natural ordering (todo)
    (println "freq" freq)
    (println "letters" (distinct sort-letters))
    (= hash (apply str (distinct sort-letters)))))

(defn day4-1 [^String input]
  (->> (str/split-lines input)
       (map to-info)
       (filter valid)
       (map second) ; Get the sector num
       (reduce +)))
