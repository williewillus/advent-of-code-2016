(ns advent-of-code-2016.day14
  (:import (java.nio ByteBuffer)
           (java.security MessageDigest)))

(def ^:private magic "ahsbgdzn")

(defn- md5-to-string [^bytes b]
  (let [upper (.getLong (ByteBuffer/wrap b 0 8))
        lower (.getLong (ByteBuffer/wrap b 8 8))]
    (str (Long/toUnsignedString upper 16)
         (Long/toUnsignedString lower 16))))

(defn- md5-hash [^String s]
  (->> (.getBytes s)
       (.digest (MessageDigest/getInstance "MD5"))
       (md5-to-string)))

(def ^:private get-info
  (memoize
    (fn [idx]
      ; given an idx, hashes magic+idx then returns
      ; 1: first char that appears 3 in a row (or nil)
      ; 2: set of what chars appear 5 in a row
      (let [h (md5-hash (str magic idx))]
        [(first (ffirst (re-seq #"([a-f\d])\1{2}" h)))
         (into #{} (map ffirst (re-seq #"([a-f\d])\1{4}" h)))]))))

(defn- is-key [idx]
  (let [[triple _] (get-info idx)]
    (and
      (some? triple)
      (->> (range (inc idx) (+ idx 1001))
           (map get-info)
           (map second)
           (some #(contains? % triple))))))

(defn day14-1 []
  ; todo (off by one?)
  (take 65 (filter is-key (range))))