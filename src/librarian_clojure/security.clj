(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use librarian-clojure.db)
  (:require [sandbar.auth :as auth]))

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

(defn log-in [request]
  (if-let [{:keys [login password]} (:params request)]
    (if-let [user (db-get-user login)]
      (if (check-password password (:password user))
        {:name login :roles (into #{} (map keyword (:roles user)))}))))
