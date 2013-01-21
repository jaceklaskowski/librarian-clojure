;; Based on https://github.com/brentonashworth/one/blob/master/src/app/clj/one/sample/repl.clj
(ns librarian-clojure.repl
  "The starting namespace for the project. This is the namespace that
  users will land in when they start a Clojure REPL. It exists to
  provide convenience functions like 'go' and 'stop'."  
  (:require [clojure.java.browse :as browse]
            [librarian-clojure.run :as run]))

(def ^:dynamic ^org.eclipse.jetty.server.Server *server*)

(defn go
  "Start a server and launch a browser to talk to it."
  []
  (let [port (-> (System/getenv) (get "PORT" "8080") Integer/parseInt)]
    (run/init :local)
    (alter-var-root #'*server* (fn [_] (run/start-server port)))
    (browse/browse-url (str "http://localhost:" port))))

(defn stop
  "Stop the server"
  [] (.stop *server*))
