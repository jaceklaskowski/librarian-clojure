(ns librarian-clojure.core
  (:use ring.adapter.jetty
        ring.handler.dump
        librarian-clojure.books)
  (:require
    [compojure.core :as compojure]
    [compojure.route :as route]
    [compojure.handler :as handler]))

(defn hello-world [app]
  (fn [req]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body "Hello, world"}))

(defn upper-case [app]
  (fn [req]
    (let [resp (app req)
          body (:body resp)
          upper-case-body (.toUpperCase body)]
      (conj resp {:body upper-case-body}))))
  
(def hello-world-app
  (-> handle-dump
    hello-world
    upper-case))

(compojure/defroutes routes
  (compojure/ANY "/" [] hello-world-app)
  (compojure/GET "/books" [] (get-books))
  (compojure/POST "/books" [id author title] (post-books id author title))
  (compojure/GET "/books/delete" [id] (delete-book id))
  (compojure/GET "/books/edit" [id] (edit-book id))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site routes))

(defn -main [port]
  (run-jetty app {:port (Integer. port)}))
