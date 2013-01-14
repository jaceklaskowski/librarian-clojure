(ns librarian-clojure.routes
  "Provides routes for the web application"
  (:require [compojure.core :refer (GET POST PUT DELETE ANY defroutes)]
            [compojure.route :refer (resources not-found)]
            [ring.util.response :refer (redirect)]
            [clojure.data.json :as json]
            [librarian-clojure.security :as security]
            [librarian-clojure.page :as page]
            [librarian-clojure.books :as books]
            [cemerick.friend :as friend]))

(defroutes routes
  (GET       "/"                  request                   (page/render-main request))
  (GET       "/login"             request                   (page/render-main request))
  (POST      "/login"             []                        (-> (security/login-handler)
                                                                json/json-str))
  (POST      "/signup"            []                        (-> (security/signup-handler)
                                                                json/json-str))
  (GET       "/books"             []                        (-> (books/get-books)
                                                                json/json-str))
  (GET "/books/:id" [request id] (page/render-book-page request id))
  (PUT       "/books"             [author title]            (friend/authorize
                                                             #{:admin}
                                                             (-> (books/add-book author title)
                                                                 json/json-str)))
  (DELETE    "/books/:id"         [id]                      (friend/authorize
                                                             #{:admin}
                                                             (-> id
                                                                 (books/delete-book)
                                                                 json/json-str)))
  (POST      "/books/:id"         [id author title]         (friend/authorize
                                                             #{:admin}
                                                             (-> (books/update-book id author title)
                                                                 json/json-str)))
  (friend/logout (ANY "/logout"   []                        (-> (security/logout-handler)
                                                                json/json-str)))
  (resources "/")
  (ANY       "/*"                 []                        (redirect "/")))
