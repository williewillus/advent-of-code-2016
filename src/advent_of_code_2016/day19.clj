(ns advent-of-code-2016.day19)

(def ^:private input 3001330)

; Josephus problem (Numberphile Oct. 28): rotate MSB to LSB position
(defn day19-1 []
  (let [lz (Integer/numberOfLeadingZeros input)
        msb-off (bit-and input (bit-not (bit-shift-left 1 (- 31 lz))))
        shifted (bit-shift-left msb-off 1)
        answer (bit-or shifted 1)]
    answer))

