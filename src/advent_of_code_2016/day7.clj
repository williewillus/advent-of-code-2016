(ns advent-of-code-2016.day7
  (:require [clojure.string :as str]))

; left brace, anything not a right brace 1+ times, right brace
(def ^:private square-brackets #"\[([^\]]+)\]")

; anything as long as it isn't followed by itself, something else x 2, the original thing
(defn- has-abba [s] (some? (re-find #"(.)(?!\1)(.)\2\1" s)))

(defn- is-tls [line]
  (let [supers (str/split line square-brackets)
        hypers (map second (re-seq square-brackets line))]
    (and
      (some has-abba supers)
      (not-any? has-abba hypers))))

(defn day7-1 [^String input]
  (->> (str/split input #"\R+")
       (filter is-tls)
       (count)))

; empty string as long as it is followed by an aba, then capture any 1 char
; this lets us have overlapping aba's by not consuming all of it
; in PCRE this would be a conditional (?(?=<aba>)(.))
(defn- create-aba-res [s]
  (let [abas (map (fn [[_ g1 g2 g3]] (str g1 g2 g3))
                  (re-seq #"(?=(.)(?!\1)(.)\1)(.)" s))]
    (map (fn [[a b a]]
           (re-pattern (str b a b))) abas)))

(defn- is-ssl [line]
  (let [supers (str/split line square-brackets)
        aba-res (mapcat create-aba-res supers)
        hypers (map second (re-seq square-brackets line))]
    (some (fn [re]
            (some #(re-find re %) hypers)) aba-res)))

(defn day7-2 [^String input]
  (->> (str/split input #"\R+")
       (filter is-ssl)
       (count)))
