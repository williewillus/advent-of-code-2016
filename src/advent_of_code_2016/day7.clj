(ns advent-of-code-2016.day7
  (:require [clojure.string :as str]))

(defn- has-abba [s] (some? (re-seq #"(.)(?!\1)(.)\2\1" s)))

(defn- is-tls [line]
  (let [supers (str/split line #"\[[^\]]+\]")
        hypers (map second (re-seq #"\[([^\]]+)\]" line))]
    (and
      (some has-abba supers)
      (not-any? has-abba hypers))))

(defn day7-1 [^String input]
  (->> (str/split input #"\R+")
       (filter is-tls)
       (count)))