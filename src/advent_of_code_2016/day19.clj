(ns advent-of-code-2016.day19)

(def ^:private input 3001330)

(defn- solve-p1 [n]
  "Josephus problem (Numberphile Oct. 28): rotate MSB to LSB position"
  (let [lz (Integer/numberOfLeadingZeros n)
        msb-off (bit-and n (bit-not (bit-shift-left 1 (- 31 lz))))
        shifted (bit-shift-left msb-off 1)
        answer (bit-or shifted 1)]
    answer))

(defn- solve-p2 [n]
  "day19-2: solved by hand while looking at /u/bblum's data table: http://pastebin.com/raw/8vdM5Jcz
   Write n = 3^k + l, where k is as large as possible.
   AKA, n = 3 ^ floor(log(n, 3)) + l
   If n = 3^k, then w(n) = 3^k
   If n <= 2(3^k), then w(n) = l
   Otherwise, let n = 3^k+1 - m, then w(n) = 3^k+1 - 2m or more simply w(n) = n - m"
  (let [k (int (/ (Math/log n) (Math/log 3)))
        three-k (Math/pow 3 k)
        l (- n three-k)]
    (cond
      (== n three-k) three-k
      (<= n (* 2 three-k)) l
      :else (let [m (- (Math/pow 3 (inc k)) n)]
              (- n m)))))

(defn day19-1 [] (solve-p1 input))

(defn day19-2 [] (solve-p2 input))

