(ns librarian-clojure.routes
  "Provides routes for the web application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources not-found)]
        [ring.util.response :only (redirect)]
        [librarian-clojure books security page])
  (:require [clojure.data.json :as json]))

(defroutes routes
  (GET       "/"                  []                        (render-login))
  (GET       "/books"             []                        (-> (get-books) json/json-str))
  (PUT       "/books"             [author title]            (-> (add-book author title) json/json-str))
  (DELETE    "/books/:id"         [id]                      (-> (delete-book id) json/json-str))
  (POST      "/books/:id"         [id author title]         (-> (update-book id author title) json/json-str))
  (POST      "/login"             [login password request]  (-> (log-in login password request) json/json-str))
  (GET       "/admin"             []                        (render-admin))
  (GET       "/permission-denied" []                        "Access denied")
  (resources "/")
  (ANY       "/*"                 []                        (redirect "/")))
