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

(defn bfs2
  "BFS out from the given source node, returning {dest nodeid: distance}"
  [layout node-to-pos srcpos]
  (let [pos-to-node (clojure.set/map-invert node-to-pos)]
    (loop [q (conj clojure.lang.PersistentQueue/EMPTY srcpos)
           seen {srcpos 0}]
      (if (seq q)
        (let [v (peek q)
              dist (seen v)
              nb (neighbors layout v)
              unseen-nbs (remove seen nb)
              nb-dist (zipmap nb (repeat (inc dist)))]
          (recur (apply conj (pop q) unseen-nbs)
                 (merge-with min seen nb-dist)))
        (into {}
              (map (fn [[node pos]] [node (seen pos)]) node-to-pos))))))


(defn add-unseen-nbs
  [layout pos q seen dist]
  (let [xf (comp
            (filter #(and (not (seen %)) (is-open layout %)))
            (map (fn [pos] [pos (inc dist)])))]
    (into q xf [(up pos) (down pos) (left pos) (right pos)])))
          
(defn bfs
  "BFS out from the given source node, returning {dest nodeid: distance}"
  [layout node-to-pos srcpos]
  (let [pos-to-node (clojure.set/map-invert node-to-pos)]
    (println (str "BFS from " (pos-to-node srcpos)))
    (loop [result (transient {})
           q (conj clojure.lang.PersistentQueue/EMPTY [srcpos 0])
           seen-pos (transient #{})]
      (if (empty? q)
        (do
          (println "Seen poses:" (count seen-pos))
          (persistent! result))
        (let [[curpos curdist] (first q)
              nextresult (if (some? (pos-to-node curpos))
                           (assoc! result (pos-to-node curpos) curdist)
                           result)
              nextq (add-unseen-nbs layout curpos (pop q) seen-pos curdist)
              nextseen (conj! seen-pos curpos)]
          (recur nextresult nextq nextseen))))))

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
  [dists visit-order]
  (->> (partition 2 1 visit-order)
       (map (fn [[from to]] ((dists from) to)))
       (apply +)))

(defn p1 [input]
  (let [[layout nodes] (load-input input)
        dists (apsp layout nodes)]
    (println "computed apsp" dists)
    (->> (comb/permutations (into [] (keys nodes)))
         (map (partial evaluate dists))
         (apply min))))
