(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use librarian-clojure.db)
  (:require [sandbar.auth :as auth]))

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

;TODO: destructure
(defn log-in [request]
  (let [login (:login (:params request))
        password (:password (:params request))]
    ;(prn login request)
    (if-let [user (db-get-user login)]
      (when (check-password password (:password user))
        {:name login :roles (into #{} (map keyword (:roles user)))})
      )
  ))
  ;(if-let [user (db-get-user login)]
    ;(when (check-password password (:password user))
     ; (prn request)
      ;(assoc-in [:session :user] user)
      ;{:successful true})
    ;{:successful false}))