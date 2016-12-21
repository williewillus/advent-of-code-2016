(ns advent-of-code-2016.day10
  (:require [clojure.string :as str]
            [clojure.core.async :refer [<! >! <!! chan go]]))

(defn- create-chans [^String input]
  (let [matches (map rest (re-seq #"(bot|output) (\d+)" input))]
    (into {} (for [k (distinct matches)]
               [k (chan 2)]))))

(defn day10 [^String input]
  (let [chans (create-chans input)]
    (doseq [line (str/split input #"\R+")]
      (condp #(str/starts-with? %2 %1) line
        "value" (let [[_ val bot] (re-find #"value (\d+) goes to bot (\d+)" line)]
                  (go (>! (chans ["bot" bot]) (Long/parseLong val))))
        "bot" (let [[_ bot lotype lonum hitype hinum] (re-find #"bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)" line)]
                (go
                  (let [ch (chans ["bot" bot])
                        [lo hi] ((juxt min max) (<! ch) (<! ch))]
                    (when (and (= 17 lo) (= 61 hi))
                      (println bot "is comparing 17 and 61"))
                    (>! (chans [lotype lonum]) lo)
                    (>! (chans [hitype hinum]) hi))))))

    (let [o0 (<!! (chans ["output" "0"]))
          o1 (<!! (chans ["output" "1"]))
          o2 (<!! (chans ["output" "2"]))]
      (println "o0 x o1 x o2:" (* o0 o1 o2)))))
