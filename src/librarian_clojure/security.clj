(ns librarian-clojure.security
  (:use [cemerick.friend.credentials :only (hash-bcrypt)])
  (:require [sandbar.stateful-session :as session]
            [librarian-clojure.db :as db]
            [cemerick.friend :as friend]))

  
;; Init - creates admin/admin superuser

(defn init []
  (when-not (db/db-get-user "admin")
    (prn "Creating privileged admin/admin user")
    (db/db-add-user "admin" (hash-bcrypt "admin") [:admin])))

;; Auth helpers

(defn- success [] {:successful true})

(defn- failure [msg] {:successful false 
                      :errorDetail msg})
(defn get-user-by-login
  "Assures that the returned data contain :username key for friend's identity."
  [login]
  (if-let [user-creds (db/db-get-user login)]
    (-> user-creds
        (assoc :username (:login user-creds)))))

(defn get-user [request]
  "Request is passed explicitly - it's strongly encouraged by friend
even though there's an option of using internal dynamic binding which
should contain identity in the context current on-the-fly request."
  (friend/current-authentication request))

(defn has-role? [request role]
  (friend/authorized? [role] (friend/identity request)))

;; Auth related handlers

(defn login-failure-handler
  [request]
  (failure "Login failed"))

(defn unauthorized-handler [request]
  [request]
  {:status 403 :body "Access denied"})

(defn signup-handler [login password request]
  (if (get-user-by-login login)
    (failure "Account exists")
    (db/db-add-user login (hash-bcrypt password))))

(defn login-handler
  "Succesful login handler must be put under POST /login route."
  []
  (success))

(defn logout-handler
  []
  (success))

