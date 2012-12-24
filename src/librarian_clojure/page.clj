(ns librarian-clojure.page
  (:use [librarian-clojure books])
  (:require [clojure.string :as s]
            [clojure.data.json :as json]
            [librarian-clojure.security :as sec]))

(defn read-file [path]
  (slurp (.getResourceAsStream Object path)))

(defn set-attr [template name value]
  (s/replace template (re-pattern (str "\\#\\{" name "\\}")) value))

(defn greet-user [{:keys [login]}]
  (str "Hello, " login))

(defn render-user []
  (let [template (read-file "/public/index.html")]
    (set-attr template "lib-booklist"  (str "var books = " (-> (get-books) json/json-str)))
    (if-let [user (sec/get-user)]
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
      (set-attr "login-form" (greet-user (sec/get-user)))
      (set-attr "logout-form" (read-file "/public/logout-form.html")))))

(defn render-main []
  (if (sec/has-role? :admin)
    (render-admin)
    (render-user)))
