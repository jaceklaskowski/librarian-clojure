(ns librarian-clojure.routes
  "Provides routes for the web application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources not-found)]
        [ring.util.response :only (redirect)]
        [librarian-clojure books page])
  (:require [clojure.data.json :as json]
            [librarian-clojure.security :as security]
            [cemerick.friend :as friend]))

(defroutes routes
  (GET       "/"                  request                   (render-main request))
  (GET       "/login"             request                   (render-main request))
  (POST      "/login"             []                        (-> (security/login-handler) json/json-str))
  (GET       "/books"             []                        (-> (get-books) json/json-str))
  (PUT       "/books"             [author title]            (-> (add-book author title) json/json-str))
  (DELETE    "/books/:id"         [id]                      (-> (delete-book id) json/json-str))
  (POST      "/books/:id"         [id author title]         (-> (update-book id author title) json/json-str))
  (friend/logout (ANY "/logout"   []                        (-> (security/logout-handler) json/json-str)))
  (POST      "/signup"            [login password request]  (-> (security/signup-handler login password request) json/json-str))
  (resources "/")
  (ANY       "/*"                 []                        (redirect "/")))
