(ns advent-of-code-2016.day22
  (:require [clojure.string :as str]))

(defn- parse [line]
  (let [[x y size use] (map #(Long/parseLong %) (rest (re-find #"\/dev\/grid\/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+\d+T" line)))]
    [[x y] {:used use :size size}]))

(defn- viable-pairs [nodes]
  (for [[ak av :as a] nodes [bk bv :as b] nodes
        :when (and (not= ak bk)
                   (pos? (:used av))
                   (<= (:used av) (- (:size bv) (:used bv))))]
    [a b]))

(defn day22-1 [^String input]
  (let [nodes (apply hash-map (mapcat parse (drop 2 (str/split input #"\R+"))))]
    (count (viable-pairs nodes))))

; this just prints it. solve manually :P
(defn day22-2 [^String input]
  (let [nodes (apply hash-map (mapcat parse (drop 2 (str/split input #"\R+"))))
        max-x (apply max (map first (map key nodes)))
        max-y (apply max (map second (map key nodes)))
        target [max-x 0]]
    (doseq [y (range (inc max-y))
            x (range (inc max-x))]
      (let [[k v] (find nodes [x y])]
        (cond
          (= k target) (print \G)
          (= k [0 0]) (print \S)
          (= (:used v) 0) (print \_)
          (> (:size v) 200) (print \#)
          :else (print \.)))
      (when (= x max-x)
        (newline)))))
