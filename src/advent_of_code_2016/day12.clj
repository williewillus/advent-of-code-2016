(ns advent-of-code-2016.day12
  (:require [clojure.string :as str]))

(defn- do-decode [^String insn]
  (let [[op r1 r2] (str/split insn #"\s+")
        inc-pc (fn [state] (update state :pc inc))]
    (case op
      "cpy" (comp inc-pc
              (if (re-matches #"\d+" r1)
                (fn [state] (assoc state (keyword r2) (Long/parseLong r1))) ; imm
                (fn [state] (assoc state (keyword r2) ((keyword r1) state))))) ; reg
      "inc" (comp inc-pc (fn [state] (update state (keyword r1) inc)))
      "dec" (comp inc-pc (fn [state] (update state (keyword r1) dec)))
      "jnz" (if (re-matches #"\d+" r1)
              ; todo dedupe this code
              (fn [state]                                    ; imm
                (if (not (zero? (Long/parseLong r1)))
                  (update state :pc (partial + (Long/parseLong r2)))
                  (inc-pc state)))
              (fn [state]                                   ; reg
                (if (not (zero? ((keyword r1) state)))
                  (update state :pc (partial + (Long/parseLong r2)))
                  (inc-pc state)))))))

; todo separate decode and evaluation stages? memoizing decode is kinda cheating
(def ^:private decode (memoize do-decode))

(defn- solve [^String input init-state]
  (let [lines (str/split input #"\R+")
        program-len (count lines)]
    (loop [{pc :pc :as state} init-state]
      (if (>= pc program-len)
        state
        (recur ((decode (nth lines pc)) state))))))

(defn day12-1 [^String input] (solve input {:a 0 :b 0 :c 0 :d 0 :pc 0}))
(defn day12-2 [^String input] (solve input {:a 0 :b 0 :c 1 :d 0 :pc 0}))