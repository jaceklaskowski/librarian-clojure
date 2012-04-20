(ns librarian-clojure.page
  (:require [clojure.string :as s]))

(defn render-login []
  (let [template (slurp (.getResourceAsStream Object "/public/index.html"))]
    (s/replace template #"lib-booklist" "s = 'Hello world'")))
