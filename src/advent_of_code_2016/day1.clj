(ns advent-of-code-2016.day1)

; unit vecs for north, east, south, west
(def ^:private directions [[0 1] [1 0] [0 -1] [-1 0]])

(defn- cw [dir] (mod (inc dir) 4))

(defn- ccw [dir] (mod (dec dir) 4))

; "L1" => [ccw 1]
(defn- convert-stringop [strop]
  [(case (first strop)
     \L ccw
     \R cw)
   (Long/parseLong (subs strop 1))])

(defn- parse [^String input]
  (map convert-stringop (re-seq #"[LR]\d+" input)))

(defn- move [[dir coord] [rotate amount]]
  (let [newdir  (rotate dir)
        movevec (map (partial * amount) (nth directions newdir))]
    [newdir (map + coord movevec)]))

(defn day1-1 [^String input]
  (let [[x y] (second (reduce move [0 [0 0]] (parse input)))]
    (+ (Math/abs x) (Math/abs y))))

(defn- all-between [[x1 y1 :as start] [x2 y2]]
  (let [result
        (cond
          (= x1 x2) (for [i (range (min y1 y2) (inc (max y1 y2)))]
                      [x1 i])
          (= y1 y2) (for [i (range (min x1 x2) (inc (max x1 x2)))]
                      [i y1])
          :else (throw (IllegalStateException. "Can't go diagonal")))]
    (filter #(not= % start) result)))

(defn day1-2 [^String input]
  (let [path (map second (reductions move [0 [0 0]] (parse input)))
        [x y] (loop [seen #{}
                    [pos & ps] path]
                (if-not (seq ps)
                  pos
                  (let [steps (all-between pos (first ps))]
                    (if-let [seen-pos (some seen steps)]
                      seen-pos
                      (recur (apply conj seen steps) ps)))))]
    (println x y)
    (+ (Math/abs x) (Math/abs y))))
