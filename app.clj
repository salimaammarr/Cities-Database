(ns app
  (:require [a3.db :refer [loadCityData]]
              [a3.menu :refer [mainMenu]]))

(def citiesDB (loadCityData "cities.txt"))

(mainMenu citiesDB)


