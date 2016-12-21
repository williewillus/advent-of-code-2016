(ns advent-of-code-2016.day21
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as comb]))

(def ^:private ^:const to-encode "abcdefgh")

(def ^:private ^:const to-decode "fbgdceah")

(defn- swap-pos [x y s]
  (let [xchar (nth s x)
        ychar (nth s y)]
    (apply str (map-indexed (fn [idx ch] (condp = idx x ychar y xchar ch)) s))))

(defn- swap-letter [xchar ychar string]
  (-> string
      (str/replace xchar \%)
      (str/replace ychar xchar)
      (str/replace \% ychar)))

(defn- rotate-left [sh s]
  (let [shift (mod sh (count s))]
    (if (zero? shift) s (.concat (subs s shift) (subs s 0 shift)))))

(defn- rotate-right [shift s]
  (rotate-left (- (dec (count s)) (dec shift)) s))

(defn- rotate-pos [ch s]
  (let [idx (str/index-of s ch)
        shift (+ idx 1 (if (>= idx 4) 1 0))]
    (rotate-right shift s)))

(defn- reverse-subseq [x y s]
  (str (subs s 0 x)
       (str/reverse (subs s x y))
       (subs s y)))

(defn- move [x y s]
  (let [ch (nth s x)]
    (str (doto
           (StringBuilder. ^String s)
           (.deleteCharAt ^int x)
           (.insert ^int y ^char ch)))))

(defn- parse [line]
  (condp #(str/starts-with? %2 %1) line
    "swap pos"     (let [[_ x y] (re-find #"swap position (\d+) with position (\d+)" line)]
                     (partial swap-pos (Long/parseLong x) (Long/parseLong y)))
    "swap letter"  (let [[_ xchar ychar] (re-find #"swap letter (\w) with letter (\w)" line)]
                     (partial swap-letter (first xchar) (first ychar)))
    "rotate left"  (let [[_ shift] (re-find #"rotate left (\d+) steps?" line)]
                     (partial rotate-left (Long/parseLong shift)))
    "rotate right" (let [[_ shift] (re-find #"rotate right (\d+) steps?" line)]
                     (partial rotate-right (Long/parseLong shift)))
    "rotate base"  (let [[_ ch] (re-find #"rotate based on position of letter (\w)" line)]
                     (partial rotate-pos (first ch)))
    "reverse"      (let [[_ x y] (re-find #"reverse positions (\d+) through (\d+)" line)]
                     (partial reverse-subseq (Long/parseLong x) (inc (Long/parseLong y)))) ; input gives inclusive upper
    "move"         (let [[_ x y] (re-find #"move position (\d+) to position (\d+)" line)]
                     (partial move (Long/parseLong x) (Long/parseLong y)))
    (throw (IllegalArgumentException. "Unknown op"))))

(defn day21 [^String input]
  (let [fns (map parse (str/split input #"\R+"))
        encode (reduce (fn [acc func] (comp func acc)) identity fns)]
    (println to-encode "encodes to" (encode to-encode))
    (let [perms (map (partial apply str) (comb/permutations to-decode))]
      (println to-decode "decodes to" (some #(when (= to-decode (encode %)) %) perms)))))
