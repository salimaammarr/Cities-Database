(ns a3.menu
  (:require [clojure.string :as str]))

(defn mainMenu [citiesDB])
(defn displayCityInfo [citiesDB]
  (print "Enter city name: ")
  (flush)
  (let [city-name (read-line)
        city-info (first (filter #(= (:city %) city-name) citiesDB))]
    (if city-info
      (println city-info)
      (println "City not found")))
  (mainMenu citiesDB))

(defn allCities [citiesDB]
  (println (map :city (sort-by :city citiesDB)))
  (mainMenu citiesDB))

(defn citiesByProvinceAndSize [citiesDB]
  (print "Enter province name: ")
  (flush)
  (let [province (read-line)
        cities (filter #(= (:province %) province) citiesDB)]
    (println (map #(vector (:city %) (:size %) (:population %))
                  (sort-by (fn [city]
                             [(- (case (:size city)
                                  "Large urban" 3
                                  "Medium" 2
                                  "Small" 1))
                              (:city city)])
                           cities))))
  (mainMenu citiesDB))

(defn citiesByProvinceAndDensity [citiesDB]
  (print "Enter province name: ")
  (flush)
  (let [province (read-line)
        cities (filter #(= (:province %) province) citiesDB)]
    (println (map #(vector (:city %) (/ (:population %) (:area %)))
                  (sort-by #(float (/ (:population %) (:area %))) cities))))
  (mainMenu citiesDB))

(defn provinces [citiesDB]
  (let [province-counts (frequencies (map :province citiesDB))
        sorted-provinces (sort-by (comp - second) province-counts)]
    (println (map #(vector (first %) (second %)) sorted-provinces))
    (println (format "Total: %d provinces, %d cities on file."
                     (count province-counts)
                     (count citiesDB))))
  (mainMenu citiesDB))

(defn provinceInfo [citiesDB]
  (let [province-populations (reduce (fn [acc {:keys [province population]}]
                                       (update acc province (fnil + 0) population))
                                     {}
                                     citiesDB)
        sorted-provinces (sort-by first province-populations)]
    (println (map #(vector (first %) (second %)) sorted-provinces)))
  (mainMenu citiesDB))

(defn citiesMenu [citiesDB]
  (println "1.1. List all cities, ordered by city name (ascending)")
  (println "1.2. List all cities for a given province, ordered by size (descending) and name (ascending)")
  (println "1.3. List all cities for a given province, ordered by population density (ascending)")
  (print "Enter an option? ")
  (flush)
  (let [option (read-line)]
    (case option
      "1.1" (allCities citiesDB)
      "1.2" (citiesByProvinceAndSize citiesDB)
      "1.3" (citiesByProvinceAndDensity citiesDB)
      (recur citiesDB))))

(defn mainMenu [citiesDB]
  (println "*** City Information Menu ***")
  (println "-----------------------------")
  (println "1. List Cities")
  (println "2. Display City Information")
  (println "3. List Provinces")
  (println "4. Display Province Information")
  (println "5. Exit")
  (print "Enter an option? ")
  (flush)
  (let [option (read-line)]
    (case option
      "1" (citiesMenu citiesDB)
      "2" (displayCityInfo citiesDB)
      "3" (provinces citiesDB)
      "4" (provinceInfo citiesDB)
      "5" (println "Good Bye")
      (recur citiesDB))))
