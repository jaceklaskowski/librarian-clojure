(ns librarian-clojure.routes
  "Provides routes for the web application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources not-found)]
        [ring.util.response :only (redirect)]
        librarian-clojure.books))

(defroutes routes
  (GET       "/"                 []                (redirect "/index.html"))
  (GET       "/books"            []                (get-books))
  (POST      "/books/add"        [author title]    (add-book author title))
  (POST      "/books/update/:id" [id author title] (update-book id author title))
  (GET       "/books/delete/:id" [id]              (delete-book id))
  (GET       "/books/edit/:id"   [id]              (edit-book id))
  (resources "/")
  (not-found "404"))
