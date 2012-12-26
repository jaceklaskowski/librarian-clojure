;; Based on https://github.com/brentonashworth/one/blob/master/src/app/clj/one/sample/repl.clj
(ns librarian-clojure.repl
  "The starting namespace for the project. This is the namespace that
  users will land in when they start a Clojure REPL. It exists to
  provide convenience functions like 'go' and 'dev-server'."  
  (:require [clojure.java.browse :as browse]
            [librarian-clojure.core :as core]))

(def ^:dynamic ^org.eclipse.jetty.server.Server *server*)

(defn go
  "Start a browser-connected REPL and launch a browser to talk to it."
  []
  (comment
  (dev/run-server)
  (future (Thread/sleep 3000)
          (browse/browse-url "http://localhost:8080/development"))
  (tools/cljs-repl))
  ;;
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (alter-var-root (var *server*) (fn [_] (core/start-server port)))
    (browse/browse-url (str "http://localhost:" port))))

(defn stop
  "Stop the server"
  [] (.stop *server*))
