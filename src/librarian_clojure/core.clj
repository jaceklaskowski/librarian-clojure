(ns librarian-clojure.core
  "Provides a -main function which will start the application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [compojure.route :only (resources)]
        [ring.adapter.jetty :only (run-jetty)]
        [ring.util.response :only (redirect)]
        librarian-clojure.books))

(defroutes routes
  (GET       "/books"      []                (get-books))
  (POST      "/books"      [id author title] (post-books id author title))
  (DELETE    "/books"      [id]              (delete-book id))
  (GET       "/books/edit" [id]              (edit-book id))
  (resources "/")
  (ANY       "/*"          []                (redirect "/books")))

(def app
  (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port :join? false})))
