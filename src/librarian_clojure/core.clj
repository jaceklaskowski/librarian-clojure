(ns librarian-clojure.core
  "Provides a -main function which will start the application"
  (:require
    [librarian-clojure.books :as librarian-books]
    [ring.adapter.jetty :as jetty]
    [ring.handler.dump :as dump]
    [compojure.core :as compojure]
    [compojure.route :as route]
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

(compojure/defroutes routes
  (compojure/ANY "/" [] hello-world-app)
  (compojure/GET "/books" [] (librarian-books/get-books))
  (compojure/POST "/books" [id author title] (librarian-books/post-books id author title))
  (compojure/GET "/books/delete" [id] (librarian-books/delete-book id))
  (compojure/GET "/books/edit" [id] (librarian-books/edit-book id))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (jetty/run-jetty app {:port port})))
