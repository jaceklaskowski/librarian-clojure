(ns librarian-clojure.core
  (:use librarian-clojure.routes
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)]))

(def app
  (site routes))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
