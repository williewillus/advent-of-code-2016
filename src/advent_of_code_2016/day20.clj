(ns advent-of-code-2016.day20
  (:require [clojure.string :as str]))

(defn- line->pair [line]
  (let [[_ min max] (re-find #"(\d+)-(\d+)" line)]
    [(Long/parseLong min) (inc (Long/parseLong max))]))     ; work with inclusive-exclusive pairs

(defn- rf [[acc banned allowed :as state]
           pair]
  (let [[a1 a2] acc
        [b1 b2] pair]
    (cond
      (and (<= a1 b1) (>= a2 b2)) state                                   ; encompassing interval
      (and (<= b1 a2) (>= b2 a2)) [[a1 b2] (+ banned (- b2 a2)) allowed]  ; direct expansion
      (> b1 a2) (do (println (str "Gap at " a2 " of length " (- b1 a2)))
                    [[a1 b2] (+ banned (- b2 a2)) (+ allowed (- b1 a2))]) ; there's a gap
      :else (throw (IllegalStateException. "uh oh")))))

(defn day20 [^String input]
  (let [pairs (->> (str/split input #"\R+")
                   (map line->pair)
                   (cons [0 0])
                   (sort))
        [acc banned allowed] (reduce rf [[0 0] 0 0] pairs)]
    (println "total allowed:" (- (Math/pow 2 32) (- banned allowed)))))
