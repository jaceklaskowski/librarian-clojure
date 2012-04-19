(ns librarian-clojure.core
  (:use [librarian-clojure routes security]
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)]))

(def app
  (-> routes
    site))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
