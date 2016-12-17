(ns advent-of-code-2016.day5
  (:import (java.security MessageDigest)))

(def ^:private md5 (MessageDigest/getInstance "MD5"))

(def ^:private hex-digits [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f])

(def ^:private magic "wtnhxymk")

; returns md5 digest if interesting, nil otherwise
(defn- interesting-hash [to-hash]
  (.reset md5)
  (.update md5 (.getBytes (str magic to-hash)))

  (let [digest (.digest md5)]
    ; the textual hash starts with 5 zeroes if the first 20 bits are 0
    ; first 20 bits => first 2 bytes (16 bits) and top half (4 bits) of the third
    (when
      (and
        (zero? (first digest))
        (zero? (second digest))
        (zero? (bit-and (unsigned-bit-shift-right (nth digest 2) 4) 0xF)))
      digest)))

(defn day5-1 []
  (->>
    (range)
    (map interesting-hash)
    (filter some?)
    (map #(nth hex-digits (bit-and (nth % 2) 0xF)))
    (take 8)))

; todo this is slow as hell :(
(defn day5-2 []
  (loop [sltn (apply vector (repeat 8 nil))
         iter 0
         found 0]
    (if (= found 8)
      sltn
      (if-let [hash (interesting-hash iter)]
        (let [pos (bit-and (nth hash 2) 0xF)
              chr (nth hex-digits (bit-and (unsigned-bit-shift-right (nth hash 3) 4) 0xF))]
          (if (and (>= pos 0) (< pos 8) (nil? (nth sltn pos)))
            (recur (assoc sltn pos chr) (inc iter) (inc found))
            (recur sltn (inc iter) found)))
        (recur sltn (inc iter) found)))))