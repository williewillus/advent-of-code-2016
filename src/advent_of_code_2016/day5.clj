(ns advent-of-code-2016.day5
  (:import (java.security MessageDigest)))

(def md5 (MessageDigest/getInstance "MD5"))
(def hex-digits [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f])

(defn- compute [^String to-hash]
  (.reset md5)
  (.update md5 (.getBytes to-hash))

  (let [digest (.digest md5)]
    ; the textual hash starts with 5 zeroes if the first 20 bits are 0
    ; first 20 bits => first 2 bytes (16 bits) and top half (4 bits) of the third
    (if
      (and
        (zero? (first digest))
        (zero? (second digest))
        (zero? (bit-and (unsigned-bit-shift-right (nth digest 2) 4) 0xF)))
      (nth hex-digits (bit-and (nth digest 2) 0xF))
      nil)))

(defn day5-1 []
  (->>
    (range)
    (map #(compute (str "wtnhxymk" %)))
    (filter #(not (nil? %)))
    (take 8)))