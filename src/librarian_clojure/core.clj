(ns librarian-clojure.core
  (:require [librarian-clojure.routes :refer (routes)]
            [librarian-clojure.friend :refer (custom-interactive-form
                                             signup-workflow)]
            [compojure.handler :refer (site)]
            [noir.session :refer (wrap-noir-session)]
            [librarian-clojure.security :as security]
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
