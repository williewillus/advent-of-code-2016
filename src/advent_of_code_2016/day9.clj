(ns advent-of-code-2016.day9
  (:require [clojure.string :as str]))

; todo rename/refactor to make this whole program self-documenting. the comments shouldn't be needed!

; returns number of characters until the next open paren
; or the length of the string itself if there is none
(defn- until-next-marker [s]
  (let [i (str/index-of s \()]
    (if (some? i) i (count s))))

(defn- parse [in recurse?]
  (loop [len 0
         input in]
    #_(println "input:" input)
    (if (empty? input)
      len
      (let [nm (until-next-marker input)]
        (if (zero? nm)
          ; marker to process
          (let [[marker chs m] (re-find #"\((\d+)x(\d+)\)" input)
                mark-len (count marker)
                chars (Long/parseLong chs)
                multiplier (Long/parseLong m)
                captured-length (if recurse?
                                  ; snip off just the substring specified by the marker and parse it
                                  (parse (subs input mark-len (+ mark-len chars)) recurse?)
                                  chars)]
            (recur (+ len (* multiplier captured-length))
                   (subs input (+ mark-len chars))))

          ; update length and go right up to the next marker (or the end)
          (recur (+ len nm) (subs input nm)))))))

(defn day9-1 [^String input] (parse (str/replace input #"\R+" "") false))

(defn day9-2 [^String input] (parse (str/replace input #"\R+" "") true))
