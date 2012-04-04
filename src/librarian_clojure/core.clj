(ns librarian-clojure.core
  (:use [librarian-clojure routes security]
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)])
  (:require [sandbar.auth :as auth]
            [sandbar.stateful-session :as session]))

(def security-policy
  [#"/admin.*"             :admin
   #".*"                   :any])

(defn my-authenticator [in]
  (prn in)
;  (auth/with-security (prn auth/*sandbar-current-user*))
  {:name "John" :roles #{:admin}})

; TODO no threading?
(def app
  ;(session/wrap-stateful-session
   ; (site
    ;  ((auth/with-security security-policy log-in) routes))))
  (-> routes
    (auth/with-security security-policy log-in)
    site
    session/wrap-stateful-session))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
