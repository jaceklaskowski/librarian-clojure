(ns librarian-clojure.run
  "Bootstrap for 'lein run'"
  (:require  [librarian-clojure.core :refer (app)]
             [clojure.java.browse :refer (browse-url)]
             [librarian-clojure.config :refer [*env*]]
             [librarian-clojure.db :as db]
             [librarian-clojure.security :as security]
             [ring.adapter.jetty :refer (run-jetty)]))

(defn- parse-port [[port] & args]
  (if-let [port-str (or port (get (System/getenv) "PORT"))]
    (Integer/parseInt port-str)
    8080))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))

(defn- start-and-browse [args url]
  (let [port (parse-port args)]
    (start-server port)
    (browse-url (str "http://localhost:" port url))))

(defn- start [args]
  (let [port (parse-port args)]
    (start-server port)))

(defn init
  "Initialize environment (wish it'd go away one day)"
  [env]
  (binding [*env* env]
    (db/init)
    (security/init)))

(defn run-local [& args]
  (init :local)
  (start-and-browse args "/"))

(defn run-heroku [& args]
  (init :heroku)
  (start args))

(defn -main [] 
  (do
    (println "Welcome to librarian-clojure!")
    (println "")
    (println "Usage (leiningen 1):")
    (println "lein run :local [port]")
    (println "\tstarts server on [port] connecting to a local MongoDB instance, and launches web browser")
    (println "lein run :heroku [port]")
    (println "\tstarts server on [port] with MongoDB set up for Heroku")
    (println "")
    (println "Usage (leiningen 2):")
    (println "lein run-local [port]")
    (println "\tstarts server on [port] connecting to a local MongoDB instance, and launches web browser")
    (println "")
    (println "If port is empty, uses PORT environment variable, or defaults to 8080.")))
