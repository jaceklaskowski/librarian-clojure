(ns librarian-clojure.run
  "Bootstrap for 'lein run'"
  (:require  [librarian-clojure.core :refer (start-server)]
             [clojure.java.browse :refer (browse-url)]
             [librarian-clojure.config :refer [*env*]]
             [librarian-clojure.db :as db]
             [librarian-clojure.security :as security]))

(defn- parse-port [[port] & args]
  (if-let [port-str (or port (get (System/getenv) "PORT"))]
    (Integer/parseInt port-str)
    8080))

(defn- start-and-browse [args url]
  (let [port (parse-port args)]
    (start-server port)
    (browse-url (str "http://localhost:" port url))))

(defn- start [args]
  (let [port (parse-port args)]
    (start-server port)))

(defn run-local [& args]
  (binding [*env* :local]
    (db/init)
    (security/init)
    (start-and-browse args "/")))

(defn run-local-ring [& args]
  (binding [*env* :local]
    (db/init)
    (security/init)))

(defn run-heroku [& args]
  (binding [*env* :heroku]
    (db/init)
    (security/init)
    (start args)))

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
