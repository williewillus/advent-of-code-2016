(ns advent-of-code-2016.day1)

; unit vecs for north, east, south, west
(def directions [[0 1] [1 0] [0 -1] [-1 0]])

(defn- cw [dir] (mod (inc dir) 4))
(defn- ccw [dir] (mod (dec dir) 4))

; "L1" => [ccw 1]
(defn- convert-stringop [strop]
  [(case (first strop)
     \L ccw
     \R cw)
   (Long/parseLong (subs strop 1))])

; Takes the input string and converts to seq of [ccw|cw amount] pairs
(defn- as-ops [^String input]
  (map convert-stringop (re-seq #"[LR]\d+" input)))

(defn- move [[dir coord] [rotate amount]]
  (let [newdir  (rotate dir)
        movevec (map (partial * amount) (nth directions newdir))]
    [newdir (map + coord movevec)]))

(defn day1-1 [^String input]
  (let [[x y] (second (reduce move [0 [0 0]] (as-ops input)))]
    (+ (Math/abs x) (Math/abs y))))

(defn day1-2 [^String input]
  (let [path (map second (reductions move [0 [0 0]] (as-ops input)))
        [x y] (loop [seen #{}
                    [pos & ps] path]
                ;(println pos)
                ;(println (first ps))
                ;(println (contains? seen (first ps)))
                (if (or (nil? ps) (contains? seen (first ps)))
                  pos
                  (recur (conj seen pos) ps)))]
    (println x y)
    (+ (Math/abs x) (Math/abs y))))