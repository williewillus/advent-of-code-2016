(ns advent-of-code-2016.day23
  (:require [clojure.string :as str]))

(defn- decode [^String insn]
  (let [[op r1 r2] (str/split insn #"\s+")
        r1-parsed (if (re-matches #"-?\d+" r1) (Long/parseLong r1) (keyword r1))]
    (case op
      "cpy" {:type :cpy :r1 r1-parsed :r2 (keyword r2)}
      "inc" {:type :inc :r1 (keyword r1)}
      "dec" {:type :dec :r1 (keyword r1)}
      "jnz" {:type :jnz :r1 r1-parsed :r2 (if (re-matches #"-?\d+" r2) (Long/parseLong r2) (keyword r2))}
      "tgl" {:type :tgl :r1 r1-parsed})))

(defn- toggle [{type :type :as insn}]
  (if (:r2 insn)
    (case type
      :jnz (assoc insn :type :cpy)
      (assoc insn :type :jnz))
    (case type
      :inc (assoc insn :type :dec)
      (assoc insn :type :inc))))

(defn- execute [[insns {pc :pc :as state}]]
  (let [{type :type r1 :r1 r2 :r2} (nth insns pc)
        inc-pc #(update % :pc inc)
        reg-or-imm #(if (keyword? %) (% state) %)]
    (case type
      :cpy (if (keyword? r2)
             (let [from (reg-or-imm r1)]
               [insns (inc-pc (assoc state r2 from))])
             [insns (inc-pc state)])
      :inc (if (keyword? r1)
             [insns (inc-pc (update state r1 inc))]
             [insns (inc-pc state)])
      :dec (if (keyword? r1)
             [insns (inc-pc (update state r1 dec))]
             [insns (inc-pc state)])
      :jnz (let [check (reg-or-imm r1)
                 jmp   (reg-or-imm r2)]
             (if-not (zero? check)
               [insns (update state :pc (partial + jmp))]
               [insns (inc-pc state)]))
      :tgl (let [target (+ pc (reg-or-imm r1))]
             (if (>= target (count insns))
               [insns (inc-pc state)]
               [(update insns target toggle) (inc-pc state)])))))

(defn- solve [^String input init-state]
  (let [lines (str/split input #"\R+")
        program-len (count lines)]
    (loop [[insns {pc :pc :as state} :as context] [(mapv decode lines) init-state]]
      (if (>= pc program-len)
        state
        (recur (execute context))))))

(defn day23-1 [^String input] (solve input {:a 7 :b 0 :c 0 :d 0 :pc 0}))
