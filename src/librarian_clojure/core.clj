(ns librarian-clojure.core
  "Provides a -main function which will start the application"
  (:use [compojure.core :only (GET POST PUT DELETE ANY defroutes)]
        [ring.adapter.jetty :only (run-jetty)]
        [compojure.route :only (resources not-found)])
  (:require [librarian-clojure.books :as librarian-books]
            [ring.handler.dump :as dump]
            [compojure.handler :as handler]))

(defn gen-resp [uri-name]
  (println uri-name "REST handler")
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "Welcome to " uri-name " Manager")})

(defn hello-world [app]
  (fn [req]
    (println req)
    (let [uri (:uri req)
          books? (.startsWith uri "/books")]
      (if books?
        (gen-resp :books)
        (gen-resp :unknown)))))

(def hello-world-app
  (-> dump/handle-dump
    hello-world))

(defroutes routes
  (ANY       "/"           []                hello-world-app)
  (GET       "/books"      []                (librarian-books/get-books))
  (POST      "/books"      [id author title] (librarian-books/post-books id author title))
  (DELETE    "/books"      [id]              (librarian-books/delete-book id))
  (GET       "/books/edit" [id]              (librarian-books/edit-book id))
  (resources "/")
  (not-found "Page not found"))

(def app
  (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port :join? false})))
