(ns advent-of-code-2016.day22
  (:require [clojure.string :as str]))

(defn- parse [line]
  (let [[x y use avail] (map #(Long/parseLong %) (rest (re-find #"\/dev\/grid\/node-x(\d+)-y(\d+)\s+\d+T\s+(\d+)T\s+(\d+)T" line)))]
    [[x y] {:used use :avail avail}]))

(defn day22-1 [^String input]
  (let [nodes (apply hash-map (mapcat parse (drop 2 (str/split input #"\R+")))) ; drop 2 useless lines of input
        pairs (for [[ak av :as a] nodes [bk bv :as b] nodes
                    :when (and (not= ak bk)
                               (pos? (:used av))
                               (<= (:used av) (:avail bv)))]
                [a b])]
    (count pairs)))
