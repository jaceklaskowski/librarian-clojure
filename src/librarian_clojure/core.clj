(ns librarian-clojure.core
  (:use [librarian-clojure routes security]
        [librarian-clojure.friend :only (custom-interactive-form)]
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)]
        [sandbar.stateful-session :only (wrap-stateful-session)])
  (:require [librarian-clojure.security :as security]
            [cemerick.friend :as friend]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]))

(def app
  (-> routes
      (friend/authenticate
       {:credential-fn (partial creds/bcrypt-credential-fn security/get-user-by-login)
        :unauthorized-handler #'security/unauthorized-handler
        :workflows [(custom-interactive-form
                     :username-field :login
                     :redirect-on-auth? false
                     :login-failure-handler #'security/login-failure-handler)]})
      wrap-stateful-session
      site))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
