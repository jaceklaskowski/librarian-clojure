(ns librarian-clojure.db
  (:use somnium.congomongo)
  (:import [jBCrypt BCrypt]))

(def ^:dynamic *database-connection* nil)

(defn init [env]
  (println "Environment: " env)
  (if (= env :heroku)
    (do
      (alter-var-root #'*database-connection* 
                      (fn [_] 
                        (make-connection :app2623043 {:host "staff.mongohq.com" :port 10056})))
      (println "Database URL: " (System/getenv "DATABASE_URL"))
      (println "Authentication result: " (authenticate *database-connection* "heroku" "passw0rd"))))
    (do
      (alter-var-root #'*database-connection*
                      (fn [_]
                        (make-connection :test {:host "127.0.0.1" :port 27017})))))

(defn- next-seq [coll]
  (when *database-connection*
    (with-mongo *database-connection*
      (:seq (fetch-and-modify :sequences {:_id coll} {:$inc {:seq 1}}
                              :return-new? true :upsert? true)))))

(defn- insert-with-id [coll el]
  (insert! coll (assoc el :_id (next-seq coll))))

;; Books

(defn db-add-book [book]
  (when *database-connection*
    (with-mongo *database-connection*
      (insert-with-id :books book))))

(defn db-get-books
  ([]
    (db-get-books {}))
  ([query]
    (when *database-connection*
      (with-mongo *database-connection*
        (fetch :books :where query)))))

(defn db-get-book [id] 
  (first (db-get-books {:_id id})))

(defn db-update-book [id book] 
  (when *database-connection*
    (with-mongo *database-connection*
      (update! :books {:_id id} book))))

(defn db-delete-book [id]
  (when *database-connection*
    (with-mongo *database-connection*
      (destroy! :books {:_id id}))))

;; Users

(defn db-add-user 
  ([login password]
    (db-add-user login password []))
  ([login password roles]
    (when *database-connection*
      (with-mongo *database-connection*
        (insert! :users {:login login :password password :roles (concat [:user] roles)})))))

(defn db-get-user [login]
  (when (and login *database-connection*)
    (with-mongo *database-connection*
      (if-let [user (fetch-one :users :where {:login login})]
        (let [roles-kwset (set (map keyword (:roles user)))]
          (assoc user :roles roles-kwset))))))

