(ns advent-of-code-2016.day1)

; unit vecs for north, east, south, west
(def directions [[0 1] [1 0] [0 -1] [-1 0]])

(defn- cw [dir] (mod (inc dir) 4))
(defn- ccw [dir] (mod (dec dir) 4))

; "L1" => [ccw 1]
(defn- convert-stringop [strop]
  [(condp = (first strop)
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