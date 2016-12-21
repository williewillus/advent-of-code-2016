(ns advent-of-code-2016.day13
  (:import (clojure.lang PersistentQueue)))

(def ^:private ^:const magic 1364)

(defn- is-open [[x y]]
  (let [hash (+ (* x x) (* 3 x) (* 2 x y) y (* y y) magic)]
    (even? (Integer/bitCount hash))))

(defn- neighbors [[x y]]
  (->> [[(inc x) y] [x (dec y)] [(dec x) y] [x (inc y)]]
       (remove #(some neg? %))
       (filter is-open)))

(defn- bfs [start goal limit]
  (loop [q (conj PersistentQueue/EMPTY start)
         seen {start 0}
         within-limits #{start}]
    (if (seq q)
      (let [v (peek q)
            dist (seen v)]
        (cond
          (= v goal) seen
          (and (some? limit) (> dist limit)) within-limits
          :else (let [nb (neighbors v)
                      unseen-nb (remove seen nb)
                      nb-dist (zipmap nb (repeat (inc dist)))]
                  (recur (apply conj (pop q) unseen-nb)
                         (merge-with min seen nb-dist)
                         (if (and (some? limit) (<= dist limit))
                           (conj within-limits v)
                           within-limits)))))
      seen)))

(defn day13-1 [] (get (bfs [1 1] [31 39] nil) [31 39]))

(defn day13-2 [] (count (bfs [1 1] nil 50)))
