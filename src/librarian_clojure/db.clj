(ns librarian-clojure.db
  (:use somnium.congomongo)
  (:import [jBCrypt BCrypt]))

(defn init [env]
  (println "Environment: " env)
  (if (= env :heroku)
    (do
      (def db 
        (make-connection :app2623043
                         {:host "staff.mongohq.com" :port 10056}))
      (println "Database URL: " (System/getenv "DATABASE_URL"))
      (println "Authentication result: " (authenticate db "heroku" "passw0rd")))
    (do
      (def db
        (make-connection :test
                         {:host "127.0.0.1" :port 27017})))))

(defn- next-seq [coll]
  (with-mongo db
    (:seq (fetch-and-modify :sequences {:_id coll} {:$inc {:seq 1}} :return-new? true :upsert? true))))

(defn- insert-with-id [coll el]
  (insert! coll (assoc el :_id (next-seq coll))))

;; Books

(defn db-add-book [book]
  (with-mongo db
    (insert-with-id :books book)))

(defn db-get-books
  ([]
    (db-get-books {}))
  ([query]
    (with-mongo db
      (fetch :books :where query))))

(defn db-get-book [id] 
  (first (db-get-books {:_id id})))

(defn db-update-book [id book] 
  (with-mongo db
    (update! :books {:_id id} book)))

(defn db-delete-book [^Integer id]
  (with-mongo db
    (destroy! :books {:_id id})))

;; Users

(defn db-add-user 
  ([login password]
    (db-add-user login password []))
  ([login password roles]
    (with-mongo db
      (insert! :users {:login login :password password :roles (concat [:user] roles)}))))

(defn db-get-user [login]
  (with-mongo db
    (if-let [user (fetch-one :users :where {:login login})]
      (let [roles-kwset (set (map keyword (:roles user)))]
        (assoc user :roles roles-kwset)))))

