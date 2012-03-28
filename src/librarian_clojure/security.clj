(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use librarian-clojure.db)
  )

(def *librarian-user*)

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

(defn log-in [login password request]
  (if-let [user (db-get-user login)]
    (when (check-password password (:password user))
      (prn request)
      ;(assoc-in [:session :user] user)
      {:successful true})
    {:successful false}))