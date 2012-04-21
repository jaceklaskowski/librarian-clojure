(ns librarian-clojure.page
  (:use [librarian-clojure books security])
  (:require [clojure.string :as s]
            [clojure.data.json :as json]))

(def ^:dynamic *template*)

(defn read-file [path]
  (slurp (.getResourceAsStream Object path)))

(defmacro with-template [path & body]
  `(binding [*template* (read-file ~path)]
     ~@body))

(defn set-attr [name value]
  (s/replace *template* (re-pattern (str "\\#\\{" name "\\}")) value))

(defn render-login []
  (with-template "/public/index.html"
    (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (get-user)]
      (set-attr "login-form" (str "Hello, " (:login user)))
      (set-attr "login-form" (read-file "/public/login_form.chtml")))))
