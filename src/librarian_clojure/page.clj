(ns librarian-clojure.page
  (:use [hiccup.page :only (html5)]
        [clojure.string :only (replace)])
  (:require [hiccup.core :as h]))

(defn render-login []
  (let [template (slurp (.getResourceAsStream Object "/public/index.html"))]
    (replace template #"lib-booklist" "s = 'Hello world'")))