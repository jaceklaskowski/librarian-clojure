(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use [ring.util.response :only (redirect)])
  (:require [sandbar.stateful-session :as session]
            [librarian-clojure.db :as db]
            [librarian-clojure.security :as security]))

;; BCrypt Helpers

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

;; Init - creates admin/admin superuser

(defn init []
  (when-not (db/db-get-user "admin")
    (prn "Creating privileged admin/admin user")
    (db/db-add-user "admin" (crypt-password "admin") [:admin])))

;; Login/signup form

(defn- success [] {:successful true})

(defn- failure [msg] {:successful false 
                      :errorDetail msg})

(defn log-in [login password request]
  (if-let [user (db/db-get-user login)]
    (when (check-password password (:password user))
      (session/session-put! :librarian-user user)
      (success))
    (failure "Login failed")))

(defn log-out []
  (session/destroy-session!)
  (success))

(defn sign-up [login password request]
  (if (db/db-get-user login)
    (failure "Account exists")
    (do
      (db/db-add-user login (crypt-password password))
      (log-in login password request))))

;; Authorization

(defn get-user [] 
  (session/session-get :librarian-user))

(defn has-role? [role]
  (if-let [user (get-user)]
    (contains? (:roles user) role)
    false))

;; Ring wrapper for policies

(defn- authorize-line [request line]
  (let [{uri :uri} request 
        [pattern role] line]
    (if (re-matches pattern uri)
      (has-role? role)
      true)))

(defn- authorize-policy [request policy]
  (empty? (filter false? (map #(authorize-line request %) policy))))

;; TODO support fancy policies like "has one of roles", "has all of roles" etc.

(defn wrap-security [handler policy]
  (fn [request]
    (if (authorize-policy request policy)
      (handler request)
      (redirect "/permission-denied"))))
