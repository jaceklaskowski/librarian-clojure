(ns librarian-clojure.core
  (:use [librarian-clojure routes security]
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)]
        [sandbar.stateful-session :only (wrap-stateful-session)]))

(def security-policy
  [[#"/admin.*" :admin]])

(def app
  (site (wrap-stateful-session ((wrap-security security-policy) routes))))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
