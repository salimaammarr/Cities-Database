(ns a3.db
  (:require [clojure.string :as str]))

(defn parseCityLine [line]
  (let [[city province size population area] (str/split line #"\|")]
    {:city city, :province province, :size size, :population (read-string population), :area (read-string area)}))

(defn loadCityData [filename]
  (vec (map parseCityLine (str/split-lines (slurp filename)))))


