(ns advent-of-code-2016.day21
  (:require [clojure.string :as str]))

(set! *warn-on-reflection* true)

(def ^:private ^:const password "abcdefgh")

(defn- swap-pos [x y s]
  (let [xchar (nth s x)
        ychar (nth s y)]
    (apply str (map-indexed (fn [idx ch] (condp = idx x ychar y xchar ch)) s))))

(defn- swap-letter [xchar ychar string]
  (as-> string s
        (str/replace s xchar \%)
        (str/replace s ychar xchar)
        (str/replace s \% ychar)))

(defn- rotate-left [sh s]
  (let [shift (mod sh (count s))]
    (if (zero? shift) s (.concat (subs s shift) (subs s 0 shift)))))

(defn- rotate-right [shift s]
  (rotate-left (- (dec (count s)) (dec shift)) s))

(defn- rotate-pos [ch s]
  (let [idx (str/index-of s ch)
        shift (+ idx 1 (if (>= idx 4) 1 0))]
    (rotate-right shift s)))

(defn- inverse-rotate-pos [ch s] s)

; inclusive y!
(defn- reverse-subseq [x y s]
  (str (subs s 0 x)
       (str/reverse (subs s x (inc y)))
       (subs s (inc y))))

(defn- move [x y s]
  (let [ch (nth s x)]
    (str (doto
           (StringBuilder. ^String s)
           (.deleteCharAt ^int x)
           (.insert ^int y ^char ch)))))

(defn- parse [inverse? line]
  (println line)
  (condp #(str/starts-with? %2 %1) line
    "swap pos"     (let [[_ x y] (re-find #"swap position (\d+) with position (\d+)" line)]
                     (partial swap-pos (Long/parseLong x) (Long/parseLong y)))
    "swap letter"  (let [[_ xchar ychar] (re-find #"swap letter (\w) with letter (\w)" line)]
                     (if inverse?
                       (partial swap-letter (first ychar) (first xchar))
                       (partial swap-letter (first xchar) (first ychar))))
    "rotate left"  (let [[_ shift] (re-find #"rotate left (\d+) steps?" line)]
                     (if inverse?
                       (partial rotate-right (Long/parseLong shift))
                       (partial rotate-left (Long/parseLong shift))))
    "rotate right" (let [[_ shift] (re-find #"rotate right (\d+) steps?" line)]
                     (if inverse?
                       (partial rotate-left (Long/parseLong shift))
                       (partial rotate-right (Long/parseLong shift))))
    "rotate base"  (let [[_ ch] (re-find #"rotate based on position of letter (\w)" line)]
                     (if inverse?
                       (partial inverse-rotate-pos (first ch))
                       (partial rotate-pos (first ch))))
    "reverse"      (let [[_ x y] (re-find #"reverse positions (\d+) through (\d+)" line)]
                     (partial reverse-subseq (Long/parseLong x) (Long/parseLong y)))
    "move"         (let [[_ x y] (re-find #"move position (\d+) to position (\d+)" line)]
                     (if inverse?
                       (partial move (Long/parseLong y) (Long/parseLong x))
                       (partial move (Long/parseLong x) (Long/parseLong y))))
    (throw (IllegalArgumentException. "Unknown op"))))

(defn day21-1 [^String input]
  (let [fns (map (partial parse false) (str/split input #"\R+"))
        encode (reduce (fn [acc func] (comp func acc)) identity fns)]
    (encode password)))

(defn- day21-2 [^String input]
  (let [fns (map (partial parse true) (str/split input #"\R+"))
        decode (reduce (fn [acc func] (comp acc func)) identity fns)]
    (decode password)))