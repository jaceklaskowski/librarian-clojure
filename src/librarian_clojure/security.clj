(ns librarian-clojure.security
  (:require [cemerick.friend.credentials :refer (hash-bcrypt)]
            [librarian-clojure.db :as db]
            [cemerick.friend :as friend]
            [clojure.data.json :as json]))
  
(defn init
  "Creates admin/admin superuser"
  []
  (when-not (db/db-get-user "admin")
    (prn "Creating privileged admin/admin user")
    (db/db-add-user "admin" (hash-bcrypt "admin") [:admin])))

(defn- success
  "Auth helper"
  [] {:successful true})

(defn- failure
  "Auth helper"
  [msg] {:successful false 
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
  (-> request friend/identity friend/current-authentication))

(defn has-role? [request role]
  (friend/authorized? [role] (friend/identity request)))

(defn login-failure-handler
  "Auth handler"
  [request]
  {:body (-> (failure "Login failed") json/json-str)})

(defn unauthorized-handler
  "Auth handler"
  [request]
  {:status 403 :body "Access denied"})

(defn signup-workflow-handler 
  "Auth handler"
  [login password success-fn]
  (if (get-user-by-login login)
    (failure "Account exists")
    (do
      (db/db-add-user login (hash-bcrypt password))
      (success-fn (get-user-by-login login)))))

(defn signup-handler
  "Auth handler"
  []
  (success))

(defn login-handler
  "Succesful login handler must be put under POST /login route."
  []
  (success))

(defn logout-handler
  "Auth handler"
  []
  (success))

