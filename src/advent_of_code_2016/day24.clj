(ns advent-of-code-2016.day24
  (:require [clojure.math.combinatorics :as comb]))

(set! *warn-on-reflection* true)

(defn up [[r c]] [(dec r) c])
(defn down [[r c]] [(inc r) c])
(defn left [[r c]] [r (dec c)])
(defn right [[r c]] [r (inc c)])

(defn is-open
  [layout [r c]]
  (let [width (count (first layout))
        height (count layout)]
    (and (>= r 0)
         (< r height)
         (>= c 0)
         (< c width)
         (not= \# ((layout r) c)))))

(defn neighbors [layout pos]
  (->> [(up pos) (down pos) (left pos) (right pos)]
       (filter #(is-open layout %))))

(defn bfs
  "BFS out from the given source position, returning {dest nodeid: distance}"
  [layout node-to-pos srcpos]
  (let [pos-to-node (clojure.set/map-invert node-to-pos)]
    (loop [q (conj clojure.lang.PersistentQueue/EMPTY srcpos)
           dists {srcpos 0}]
      (if (seq q)
        (let [v (peek q)
              dist (dists v)
              nb (neighbors layout v)
              unseen-nbs (remove dists nb)
              nb-dist (zipmap nb (repeat (inc dist)))]
          (recur (apply conj (pop q) unseen-nbs)
                 (merge-with min dists nb-dist)))
        (into {}
              (map (fn [[node pos]] [node (dists pos)]) node-to-pos))))))

(defn apsp
  "Perform all-pairs shortest path between all members of nodes, returning {src nodeid: {dest nodeid: distance}}"
  [layout nodes]
  (into {} (map (fn [[srcid srcpos]] [srcid (bfs layout nodes srcpos)]) nodes)))

(defn update-nodes [nodes line currow]
  (->> (map-indexed vector line)
       (reduce (fn [nodes [col ch]]
                 (let [numeric-val (- (int ch) (int \0))]
                   (if (and (>= numeric-val 0) (<= numeric-val 9))
                     (assoc nodes numeric-val [currow col])
                     nodes)))
               nodes)))

(defn load-input
  "Loads the input string, returning [layout, {node: [row,col]}]"
  [input]
  (let [f (fn [[layout nodes currow] line]
            [(conj layout (vec line)) (update-nodes nodes line currow) (inc currow)])]
    (reduce f [[] {} 0] (clojure.string/split-lines input))))

(defn evaluate
  [dists p2 visit-order]
  (let [res (->> (partition 2 1 visit-order)
                 (map (fn [[from to]]
                        ((dists from) to)))
                 (apply +))
        zero-to-first ((dists 0) (first visit-order))
        last-to-zero ((dists (last visit-order)) 0)]
    (+ res zero-to-first (if p2 last-to-zero 0))))

(defn p1 [input]
  (let [[layout nodes] (load-input input)
        dists (apsp layout nodes)]
    (let [to-shuffle (into [] (filter #(not= 0 %) (keys nodes)))]
      (->> (comb/permutations to-shuffle)
           (map (partial evaluate dists false))
           (apply min)))))

(defn p2 [input]
  (let [[layout nodes] (load-input input)
        dists (apsp layout nodes)]
    (let [to-shuffle (into [] (filter #(not= 0 %) (keys nodes)))]
      (->> (comb/permutations to-shuffle)
           (map (partial evaluate dists true))
           (apply min)))))
