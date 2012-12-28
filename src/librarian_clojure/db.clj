(ns librarian-clojure.db
  (:import [jBCrypt BCrypt])
  (:use [librarian-clojure.config :only [*env*]])
  (:require [somnium.congomongo :as cm]))

(defn init []
  (println "Environment: " *env*)
  (if (= *env* :heroku)
    (let [conn (cm/make-connection :app2623043 {:host "staff.mongohq.com" :port 10056})]
      (println "Database URL: " (System/getenv "DATABASE_URL"))
      (println "Authentication result: " (cm/authenticate conn "heroku" "passw0rd"))
      (cm/set-connection! conn))
    (cm/set-connection! (cm/make-connection :test {:host "127.0.0.1" :port 27017}))))

(defn- next-seq [coll]
  (:seq (cm/fetch-and-modify :sequences {:_id coll} {:$inc {:seq 1}}
                             :return-new? true :upsert? true)))

(defn- insert-with-id [coll el]
  (cm/insert! coll (assoc el :_id (next-seq coll))))

;; Books

(defn db-add-book [book]
  (insert-with-id :books book))

(defn db-get-books
  ([]
     (db-get-books {}))
  ([query]
     (cm/fetch :books :where query)))

(defn db-get-book [id] 
  (first (db-get-books {:_id id})))

(defn db-update-book [id book] 
  (cm/update! :books {:_id id} book))

(defn db-delete-book [id]
  (cm/destroy! :books {:_id id}))

;; Users

(defn db-add-user 
  ([login password]
     (db-add-user login password []))
  ([login password roles]
     (cm/insert! :users {:login login :password password :roles (concat [:user] roles)})))

(defn db-get-user [login]
  (when login
    (if-let [user (cm/fetch-one :users :where {:login login})]
      (let [roles-kwset (set (map keyword (:roles user)))]
        (assoc user :roles roles-kwset)))))

