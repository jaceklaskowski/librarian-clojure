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

(defn greet-user [user]
  (str "Hello, " (:login user)))

(defn render-user []
  (with-template "/public/index.html"
    (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (get-user)]
      (set-attr "login-form" (greet-user user))
      (set-attr "login-form" (read-file "/public/login_form.chtml")))))

(defn render-admin []
  (with-template "/public/admin.html"
    (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (set-attr "login-form" (greet-user (get-user)))))

(defn render-main []
  (if (has-role? :admin)
    (render-admin)
    (render-user)))
