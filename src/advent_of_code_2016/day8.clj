(ns advent-of-code-2016.day8
  (:require [clojure.string :as str]))

(def ^:private width 50)

(def ^:private height 6)

(def ^:private present \u2588)

(def ^:private absent \.)

(defn- rect [w h state]
  (map-indexed
    (fn [rownum row]
      (if (< rownum h)
        (into [] (map-indexed (fn [colnum elem]
                                (if (< colnum w) present elem)) row))
        row))
    state))

(defn- rot-col [c s state]
  (let [shift (mod s height)
        old-col (map #(nth % c) state)
        rotated-col (concat (take-last shift old-col) (drop-last shift old-col))]
    (into [] (map-indexed (fn [rownum row]
                            (update row c (fn [_] (nth rotated-col rownum))))
                          state))))

(defn- rot-row [r s state]
  (let [shift (mod s width)]
    (into [] (map-indexed (fn [rownum row]
                            (if (= rownum r)
                              (into [] (concat (take-last shift row) (drop-last shift row)))
                              row))
                          state))))

(defn- parse [line]
  (condp #(str/starts-with? %2 %1) line
    "rect"          (let [[_ w h] (re-find #"rect (\d+)x(\d+)" line)]
                      (partial rect (Long/parseLong w) (Long/parseLong h)))
    "rotate column" (let [[_ c s] (re-find #"rotate column x=(\d+) by (\d+)" line)]
                      (partial rot-col (Long/parseLong c) (Long/parseLong s)))
    "rotate row"    (let [[_ r s] (re-find #"rotate row y=(\d+) by (\d+)" line)]
                      (partial rot-row (Long/parseLong r) (Long/parseLong s)))))

(defn day8 [^String input]
  (let [insns (map parse (str/split input #"\R+"))
        final-state (reduce (fn [state insn] (insn state))
                            (into [] (repeat height (into [] (repeat width absent))))
                            insns)]
    (println "Lit:" (reduce + (map (fn [row]
                                     (count (filter #(= % present) row)))
                                   final-state)))
    (map #(apply str %) final-state)))