(ns advent-of-code-2016.day13
  (:require [clojure.data.priority-map :refer [priority-map]]))

(def ^:private magic 1364)

(defn- tile-type [[x y]]
  (let [hash (+ (* x x) (* 3 x) (* 2 x y) y (* y y) magic)]
    (if (even? (Integer/bitCount hash))
      :open
      :wall)))

(defn- neighbors [[x y]]
  (let [xp (inc x) xn (dec x)
        yp (inc y) yn (dec y)]
    (->> [[xp y] [x yn] [xn y] [x yp]]
         (remove #(some neg? %))
         (filter #(= :open (tile-type %))))))

; Dijkstra in Clojure adapted from http://www.ummels.de/2014/06/08/dijkstra-in-clojure/

(defn- map-vals [m f]
  (into {}
    (for [[k v] m]
      [k (f v)])))

(defn- remove-keys [m pred]
  (select-keys m (remove pred (keys m))))

(defn- dijkstra [start nb]
  (loop [q (priority-map start 0)
         seen {}]
    (if-let [[node dist-from-start] (peek q)]               ; if there is more on the queue
      (let [new-distances
            (-> (zipmap (nb node) (repeat 1))               ; take neighbors (assuming edge cost 1)
                (remove-keys seen)                          ; without the ones we've seen
                (map-vals (partial + dist-from-start)))]    ; get total distance from start by adding on this edge's cost
        (recur
          (merge-with min (pop q) new-distances)            ; pop this node off the queue then update distances by merging in new-distances, taking the min value if there are conflicts
          (assoc seen node dist-from-start)))               ; update the seen map with [node, dist from start]
      seen)))                                               ; if queue is empty, seen has all info

; End Dijkstra

(defn day13-1 [] (get (dijkstra [1 1] neighbors) [31 39]))