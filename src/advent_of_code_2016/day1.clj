(ns advent-of-code-2016.day1)

(defn- north [[x y] amount] [x (+ y amount)])
(defn- south [[x y] amount] [x (- y amount)])
(defn- east [[x y] amount] [(+ x amount) y])
(defn- west [[x y] amount] [(- x amount) y])

(defn- ccw [dir]
  (condp = dir
    north west
    west south
    south east
    east north))

(defn- cw [dir]
  (condp = dir
    north east
    east south
    south west
    west north))

(defn- convert-stringop [strop]
  [(condp = (first strop)
     \L ccw
     \R cw)
   (Long/parseLong (subs strop 1))])

(defn- as-ops [^String input]
  (map convert-stringop (re-seq #"[LR]\d+" input)))

(defn- move [[dir coord] op]
  (let [newdir ((first op) dir)
        amount (second op)]
    ; cheating with the function symbols feels a bit out of hand here
    [newdir (newdir coord amount)]))

(defn day1-1 [^String input]
  (let [[x y] (second (reduce move [north [0 0]] (as-ops input)))]
    (+ (Math/abs x) (Math/abs y))))