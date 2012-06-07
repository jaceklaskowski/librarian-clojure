(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use librarian-clojure.db
        [ring.util.response :only (redirect)])
  (:require [sandbar.stateful-session :as session]))

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

(defn log-in [login password request]
    (if-let [user (db-get-user login)]
      (when (check-password password (:password user))
        (session/session-put! :librarian-user user)
        {:successful true})))

(defn get-user [] 
  (session/session-get :librarian-user))

(defn has-role? [role]
  (if-let [user (get-user)]
    (contains? (:roles user) role)
    false))

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
