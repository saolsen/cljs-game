;; Development Server, as of now production will probably be hosted on s3
(ns game.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defroutes app
  (GET "/" [] (.openStream (io/resource "public/index.html")))
  (route/resources "/")
  (route/not-found "Page not found"))
