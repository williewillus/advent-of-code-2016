(ns advent-of-code-2016.day17
  (:import (java.security MessageDigest)
           (clojure.lang PersistentQueue)))

(def ^:private direction->idx {\U 0 \D 1 \L 2 \R 3})

(def ^:private open-char? #{\b \c \d \e \f})

(def ^:private ^:const magic "hhhxzeay")

(let [md5 (MessageDigest/getInstance "MD5")]
  (defn- md5-hash [^String input]
    (.reset md5)
    (.update md5 (.getBytes input))
    (->> (.digest md5)
         (BigInteger. 1)
         (format "%032x"))))

(defn- valid-directions [^String path]
  (let [hash-head (subs (md5-hash path) 0 4)]
    (->> (keys direction->idx)
         (filter #(open-char? (nth hash-head (direction->idx %))))
         (into #{}))))

(defn- in-bounds [[x y]]
  (and (<= 0 x 3) (<= 0 y 3)))

(defn- neighbors [[x y] path]
  (let [valid-dir? (valid-directions path)]
    (->> {[x (inc y)] (str path \U) [x (dec y)] (str path \D)
          [(inc x) y] (str path \R) [(dec x) y] (str path \L)}
         (filter (fn [[k v]] (and (in-bounds k) (valid-dir? (last v))))))))

; we don't need a "seen" set/map in this case
; this is because we want to repeatedly revisit the "same" nodes using different path hashes
; the graph will exhaust itself as path hashes get to dead ends via valid-directions
; thank goodness it's 4x4...
(defn- bfs [start goal start-path]
  (loop [q (conj PersistentQueue/EMPTY [start start-path])
         paths #{}]
    (if (seq q)
      (let [[pos path] (peek q)
            nb (neighbors pos path)]
        (if (= pos goal)
          (recur (pop q) (conj paths path))       ; don't add to queue (ending this part of the traversal) and record a success
          (recur (apply conj (pop q) nb) paths))) ; update queue, keep going
      paths)))

(defn day17 []
  (let [paths (bfs [0 3] [3 0] magic)
        sorted (sort-by count paths)]
    (println (first sorted))
    (println (- (count (last sorted)) (count magic)))))
