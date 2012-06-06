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

; TODO https://github.com/jaceklaskowski/librarian-clojure/issues/19
; rename render-login
; copy index.html to admin.html
; remove JS and buttons from index.html
; plug it in from routes.clj

(defn render-user []
  (with-template "/public/index.html"
    (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (get-user)]
      (set-attr "login-form" (str "Hello, " (:login user)))
      (set-attr "login-form" (read-file "/public/login_form.chtml")))))

(defn render-admin []
  (with-template "/public/admin.html"
    (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (get-user)]
      (set-attr "login-form" (str "Hello, " (:login user)))
      (set-attr "login-form" (read-file "/public/login_form.chtml")))))

(defn render-main []
  (if (has-role? :admin)
    (render-admin)
    (render-user)))
