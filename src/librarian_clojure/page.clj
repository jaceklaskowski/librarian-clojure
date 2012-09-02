(ns librarian-clojure.page
  (:use [librarian-clojure books security])
  (:require [clojure.string :as s]
            [clojure.data.json :as json]))

(defn read-file [path]
  (slurp (.getResourceAsStream Object path)))

(defn set-attr [template name value]
  (s/replace template (re-pattern (str "\\#\\{" name "\\}")) value))

(defn greet-user [user]
  (str "Hello, " (:login user)))

(defn render-user []
  (let [template (read-file "/public/index.html")]
    (set-attr template "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (get-user)]
      (-> template
        (set-attr "login-form" (greet-user user))
        (set-attr "logout-form" (read-file "/public/logout-form.html")))
      (-> template
        (set-attr "login-form" (read-file "/public/login_form.chtml"))
        (set-attr "logout-form" "")))))

(defn render-admin []
  (let [template (read-file "/public/admin.html")]
    (-> template
      (set-attr "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
      (set-attr "login-form" (greet-user (get-user)))
      (set-attr "logout-form" (read-file "/public/logout-form.html")))))

(defn render-main []
  (if (has-role? :admin)
    (render-admin)
    (render-user)))
