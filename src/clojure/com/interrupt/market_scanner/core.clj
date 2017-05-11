(ns com.interrupt.market-scanner.core
  (:require  [com.stuartsierra.component :as component]
             [system.repl :refer [set-init! init start stop reset refresh system]]
             [system.components.repl-server :refer [new-repl-server]]
             [clojure.string :as str]
             [clojure.core.async :refer [chan >! <! merge go go-loop pub sub unsub-all sliding-buffer]]
             [clojure.core.match :refer [match]]
             [clojure.spec :as s]
             [clojure.spec.gen :as sg]
             [clojure.spec.test :as st]
             [clojure.future :refer :all]
             [com.rpl.specter :refer [transform select ALL]]
             [clojure.set :as cs]
             [clojure.math.combinatorics :as cmb]
             [clojure.pprint :refer [pprint]])
  (:import [java.util.concurrent TimeUnit]))


(defn system-map []
  (component/system-map
   :nrepl (new-repl-server 7888 "0.0.0.0")))

(set-init! #'system-map)
(defn start-system [] (start))
(defn stop-system [] (stop))


(comment

  ;; These can be channels pulled from Onyx input
  (def high-opt-imp-volat (atom {}))
  (def high-opt-imp-volat-over-hist (atom {}))
  (def hot-by-volume (atom {}))
  (def top-volume-rate (atom {}))
  (def hot-by-opt-volume (atom {}))
  (def opt-volume-most-active (atom {}))
  (def combo-most-active (atom {}))
  (def most-active-usd (atom {}))
  (def hot-by-price (atom {}))
  (def top-price-range (atom {}))
  (def hot-by-price-range (atom {}))


  (def sone (set (map :symbol (vals @high-opt-imp-volat))))
  (def stwo (set (map :symbol (vals @high-opt-imp-volat-over-hist))))
  (def s-volatility (cs/intersection sone stwo))  ;; OK

  (def sthree (set (map :symbol (vals @hot-by-volume))))
  (def sfour (set (map :symbol (vals @top-volume-rate))))
  (def sfive (set (map :symbol (vals @hot-by-opt-volume))))
  (def ssix (set (map :symbol (vals @opt-volume-most-active))))
  (def sseven (set (map :symbol (vals @combo-most-active))))
  (def s-volume (cs/intersection sthree sfour #_sfive #_ssix #_sseven))

  (def seight (set (map :symbol (vals @most-active-usd))))
  (def snine (set (map :symbol (vals @hot-by-price))))
  (def sten (set (map :symbol (vals @top-price-range))))
  (def seleven (set (map :symbol (vals @hot-by-price-range))))
  (def s-price-change (cs/intersection seight snine #_sten #_seleven))


  (def intersection-subsets
    (filter (fn [e] (> (count e) 1))
            (cmb/subsets [{:name "one" :val sone}
                            {:name "two" :val stwo}
                            {:name "three" :val sthree}
                            {:name "four" :val sfour}
                            {:name "five" :val sfive}
                            {:name "six" :val ssix}
                            {:name "seven" :val sseven}
                            {:name "eight" :val seight}
                            {:name "nine" :val snine}
                            {:name "ten" :val sten}
                            {:name "eleven" :val seleven}])))

  (def sorted-intersections
    (sort-by #(count (:intersection %))
             (map (fn [e]
                    (let [result (apply cs/intersection (map :val e))
                          names (map :name e)]
                      {:names names :intersection result}))
                  intersection-subsets)))

  (def or-volatility-volume-price-change
    (filter (fn [e]
              (and (> (count (:intersection e)) 1)
                   (some #{"one" "two"} (:names e))
                   (some #{"three" "four" "five" "six" "seven"} (:names e))
                   (some #{"eight" "nine" "ten" "eleven"} (:names e))))
            sorted-intersections)))


(defn -main [& args]
  (start-system))

