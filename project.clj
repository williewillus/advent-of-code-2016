(defproject advent_of_code_2016 "0.1.0-SNAPSHOT"
  :description "Advent of Code 2016 - williewillus"
  :url "http://github.com/williewillus/advent-of-code-2016"
  :license {:name "WTFPL-2.0"
            :url "http://www.wtfpl.net/about/"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [org.clojure/math.combinatorics "0.1.3"]
                 [org.clojure/core.async "0.2.395"]
                 ]
  :main ^:skip-aot advent-of-code-2016.core ; todo actually make a core ns "launcher" for all days
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
