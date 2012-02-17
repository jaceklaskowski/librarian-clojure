(ns librarian-clojure.run
  "Bootstrap for 'lein run'"
  (:use [librarian-clojure.core]
        [clojure.java.browse :only (browse-url)]))

(defn- parse-port [[port] & args]
  (Integer. 
    (or port (get (System/getenv) "PORT") 8080)))

(defn- start-and-browse [args url]
  (do 
    (let [port (parse-port args)]
      (start-server port)
      (browse-url (str "http://localhost:" port url)))))

(defn run-root [& args]
  (start-and-browse args "/"))

(defn run-books [& args]
  (start-and-browse args "/books"))

(defn -main [] 
  (do
    (println "Welcome to librarian-clojure!")
    (println "")
    (println "Usage:")
    (println "lein run :root [port]")
    (println "\tstarts server on [port] and launch web browser on the root page")
    (println "lein run :books [port]")
    (println "\tstarts server on [port] and launch web browser on the /books page")
    (println "")
    (println "If port is empty, uses PORT environment variable, or defaults to 8080.")))
