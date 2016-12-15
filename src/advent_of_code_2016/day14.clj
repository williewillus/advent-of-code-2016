(ns advent-of-code-2016.day14
  (:import (java.nio ByteBuffer)
           (java.security MessageDigest)))

(def ^:private magic "ahsbgdzn")

(defn- md5-to-string [^bytes b]
  (let [bint (BigInteger. 1 b)]
    (format "%032x" bint)))

(defn- md5-hash [^String s]
  (->> (.getBytes s)
       (.digest (MessageDigest/getInstance "MD5"))
       (md5-to-string)))

(defn- many-hash [^String init]
  (nth (iterate md5-hash init) 2017))

(def ^:private get-info
  (memoize
    (fn [hasher idx]
      ; given an idx, hashes magic+idx then returns
      ; 1: first char that appears 3 in a row (or nil)
      ; 2: set of what chars appear 5 in a row
      (let [h (hasher (str magic idx))]
        [(first (ffirst (re-seq #"([a-f\d])\1{2}" h)))
         (into #{} (map ffirst (re-seq #"([a-f\d])\1{4}" h)))]))))

(defn- is-key [hasher idx]
  (let [[triple _] (get-info hasher idx)]
    (and
      (some? triple)
      (->> (range (inc idx) (+ idx 1001))
           (map (partial get-info hasher))
           (map second)
           (some #(contains? % triple))))))

(defn day14-1 []
  (take 64 (filter (partial is-key md5-hash) (range))))

(defn day14-2 []
  (take 64 (filter (partial is-key many-hash) (range))))