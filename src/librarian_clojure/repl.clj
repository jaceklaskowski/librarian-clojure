;; Based on https://github.com/brentonashworth/one/blob/master/src/app/clj/one/sample/repl.clj
(ns librarian-clojure.repl
  "The starting namespace for the project. This is the namespace that
  users will land in when they start a Clojure REPL. It exists to
  provide convenience functions like 'go' and 'dev-server'."  
  (:require [clojure.java.browse :as browse]
            [librarian-clojure.core :as core]))

(defn go
  "Start a browser-connected REPL and launch a browser to talk to it."
  []
  (comment
  (dev/run-server)
  (future (Thread/sleep 3000)
          (browse/browse-url "http://localhost:8080/development"))
  (tools/cljs-repl)))

(println)
(println "Type (go) to launch the development server and setup a browser-connected REPL.")
(println "Type (dev-server) to launch only the development server.")
(println)
