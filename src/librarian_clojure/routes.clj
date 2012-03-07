(ns librarian-clojure.routes
  "Provides routes for the web application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources not-found)]
        [ring.util.response :only (redirect)]
        librarian-clojure.books))

(defroutes routes
  (GET       "/"                 []                (redirect "/index.html"))
  (GET       "/books"            []                (get-books))
  (PUT       "/books"            [author title]    (add-book author title))
  (DELETE    "/books/:id"        [id]              (delete-book id))
  (POST      "/books/:id"        [id author title] (update-book id author title))
  (resources "/")
  (ANY       "/*"                []                (redirect "/books")))
