(ns advent-of-code-2016.day4
  (:require [clojure.string :as str]))

; Clears cruft out of the line and converts types
(defn- to-info [^String line]
  (let [[ln name sector hash] (re-find #"([a-z\-]+)-(\d+)\[([a-z]+)\]" line)]
    [name (Long/parseLong sector) hash]))

(defn- valid
  [[name sector hash]]
  (let [clean-name (str/replace name "-" "")
        freq (frequencies clean-name)
        sort-letters (sort-by (juxt (comp - freq) identity) (distinct clean-name))]
        ; first sort by descending (negative) freq, then by forward natural order
    (= hash (apply str (take 5 sort-letters)))))

(defn day4-1 [^String input]
  (->> (str/split-lines input)
       (map to-info)
       (filter valid)
       (map second) ; Get the sector num
       (reduce +)))

(defn- rotate [lower-letter amount]
  (let [zbase (- (int lower-letter) (int \a))]
    (char (+ (int \a) (mod (+ zbase amount) 26)))))

(defn- decipher [[name sector hash]]
  [(apply str (map #(if (= % \-) \space (rotate % sector)) name)) sector hash])

(defn day4-2 [^String input]
  (->> (str/split input #"\R+")
       (map to-info)
       (filter valid)
       (map decipher)
       (filter #(str/includes? (first %) "north"))))