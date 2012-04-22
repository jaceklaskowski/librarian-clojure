(ns librarian-clojure.security
  (:import [jBCrypt BCrypt])
  (:use librarian-clojure.db))

; TODO Not quite as clean and functional as session, but hey something is working!
(def global-evil-user (ref nil))

(defn crypt-password [pw]
  (BCrypt/hashpw pw (BCrypt/gensalt 12)))

(defn check-password [candidate crypt]
  (BCrypt/checkpw candidate crypt))

(defn log-in [login password request]
    (if-let [user (db-get-user login)]
      (when (check-password password (:password user))
        (dosync (ref-set global-evil-user user))
        {:successful true})))

(defn get-user [] 
  @global-evil-user)