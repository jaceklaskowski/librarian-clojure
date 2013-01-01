(ns librarian-clojure.core
  (:use [librarian-clojure.routes :only (routes)]
        [librarian-clojure.friend :only (custom-interactive-form
                                         signup-workflow)]
        [compojure.handler :only (site)]
        [ring.adapter.jetty :only (run-jetty)]
        [noir.session :only (wrap-noir-session)])
  (:require [librarian-clojure.security :as security]
            [cemerick.friend :as friend]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]))

(def app
  (-> routes
      (friend/authenticate
       {:credential-fn (partial creds/bcrypt-credential-fn security/get-user-by-login)
        :unauthorized-handler security/unauthorized-handler
        :workflows [(custom-interactive-form
                     :username-field :login
                     :redirect-on-auth? false
                     :login-failure-handler #'security/login-failure-handler)
                    signup-workflow]})
      wrap-noir-session
      site))

(defn start-server [port]
  (run-jetty #'app {:port port :join? false}))
