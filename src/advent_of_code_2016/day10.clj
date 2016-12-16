(ns advent-of-code-2016.day10
  (:require [clojure.string :as str]))

; state: { robotID(int) -> #{chips}, :outX -> #{chips} }
; insn: { :bot robotID :low (robotID|:outX) :high (robotID|:outX) }

(defn- take-chip [loc chip state]
  (if (contains? state loc)
    (update state loc (fn [old] (disj old chip)))
    state))

(defn- give-chip [loc chip state]
  (update state loc (fn [old]
                       (if (some? old) (conj old chip) #{chip}))))

(defn- create-location [s]
  (cond
    (str/starts-with? s "output ") (keyword (str "out" (subs s 7)))
    (str/starts-with? s "bot ") (keyword (str "bot" (subs s 4)))))

(defn- parse-line [insns line]
  (if (str/starts-with? line "value")
    (let [[_ ch b] (re-find #"value (\d+) goes to (bot \d+)" line)
          chip (Long/parseLong ch)
          bot (create-location b)]
      (conj insns {:type :direct :chip chip :bot bot}))
    (let [[_ bot lo hi] (re-find #"(bot \d+) gives low to (bot \d+|output \d+) and high to (bot \d+|output \d+)" line)]
      (conj insns {:type :give :bot (create-location bot) :low (create-location lo) :high (create-location hi)}))))

; fixme
(defn- execute [state insn]
  (let [{bot :bot lo-dest :low hi-dest :high} insn
        bot-inv (bot state)
        lo-chip (apply min bot-inv)
        hi-chip (apply max bot-inv)]
    (println insn bot-inv)
    (when (and (= 17 lo-chip) (= 61 hi-chip))
      (println bot "is comparing 17 and 61!"))
    (->> state
         (take-chip bot lo-chip)
         (take-chip bot hi-chip)
         (give-chip lo-dest lo-chip)
         (give-chip hi-dest hi-chip))))

(defn- day10-1 [^String input]
  (let [insns (reduce parse-line [] (str/split input #"\R+"))]
    (reduce execute {} insns)))