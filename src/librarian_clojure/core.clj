(ns librarian-clojure.core
  "Provides routes for the web application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources)]
        [ring.adapter.jetty :only (run-jetty)]
        [ring.util.response :only (redirect)]
        librarian-clojure.books
        [compojure.handler :only (site)]))

(defroutes routes
  (GET       "/books"      []                (get-books))
  (POST      "/books"      [id author title] (post-books id author title))
  (DELETE    "/books"      [id]              (delete-book id))
  (GET       "/books/edit" [id]              (edit-book id))
  (resources "/")
  (ANY       "/*"          []                (redirect "/books")))

(def app
  (site routes))

(defn start-server [port]
  (run-jetty app {:port port :join? false}))
