(ns advent-of-code-2016.day9
  (:require [clojure.string :as str]))

(defn day9-1 [^String in]
  (let [input (str/replace in #"\R+" "")]
    (loop [cursor 0 len 0
           state :default
           marker-acc []]
      (if (>= cursor (count input))
        len
        (let [ch (nth input cursor)]
          (case state
            :default (if (= ch \()
                                 (recur (inc cursor) len :in-marker marker-acc)
                                 (recur (inc cursor) (inc len) state marker-acc))

            :in-marker (if (= ch \))
                                   (let [[amt rpt] (map #(Long/parseLong %)
                                                        (str/split (apply str marker-acc) #"x"))]
                                     (recur (+ cursor (inc amt)) (+ len (* amt rpt)) :default []))
                                   (recur (inc cursor) len state (conj marker-acc ch)))))))))
