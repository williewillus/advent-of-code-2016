(ns advent-of-code-2016.day10
  (:require [clojure.string :as str]
            [clojure.core.async :refer [<! >! <!! chan go]]))

(defn- key-from-type [type num]
  (case type
    "bot" (keyword (str "bot" (Long/parseLong num)))
    "output" (keyword (str "out" (Long/parseLong num)))))

(defn- create-chans [^String input]
  (let [matches (map (partial drop 1) (re-seq #"(bot|output) (\d+)" input))]
    (loop [[[type num :as x] & xs] matches
           chs (transient {})]
      (if (seq x)
        (let [k (key-from-type type num)]
          (if-not (chs k)
            (recur xs (assoc! chs k (chan 2)))
            (recur xs chs)))
        (persistent! chs)))))

(defn day10 [^String input]
  (let [chans (create-chans input)]
    (doseq [line (str/split input #"\R+")]
      (condp #(str/starts-with? %2 %1) line
        "value" (let [[_ val bot] (re-find #"value (\d+) goes to bot (\d+)" line)]
                  (go (>! (chans (key-from-type "bot" bot)) (Long/parseLong val))))
        "bot" (let [[_ bot lotype lonum hitype hinum] (re-find #"bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)" line)]
                (go
                  (let [ch (chans (key-from-type "bot" bot))
                        n1 (<! ch) n2 (<! ch)
                        max (max n1 n2) min (min n1 n2)
                        loch (chans (key-from-type lotype lonum))
                        hich (chans (key-from-type hitype hinum))]
                    (when (and (= 17 min) (= 61 max))
                      (println bot "is comparing 17 and 61"))
                    (>! loch min)
                    (>! hich max))))))

    (let [o0 (<!! (chans :out0))
          o1 (<!! (chans :out1))
          o2 (<!! (chans :out2))]
      (println "o0 x o1 x o2:" (* o0 o1 o2)))))
