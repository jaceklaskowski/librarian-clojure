(ns librarian-clojure.db
  (:use somnium.congomongo))

(defn init [env]
  (println "Environment: " env)
  (if (= env :heroku)
    (do
      (def db 
        (make-connection :app2623043
                         {:host "staff.mongohq.com" :port 10056}))
      (println "Database URL: " (System/getenv "DATABASE_URL"))
      (println "Authentication result: " (authenticate db "heroku" "passw0rd")))
    (def db
      (make-connection :test
                       {:host "127.0.0.1" :port 27017}))))

(defn- next-seq [coll]
  (with-mongo db
    (:seq (fetch-and-modify :sequences {:_id coll} {:$inc {:seq 1}} :return-new? true :upsert? true))))

(defn- insert-with-id [coll el]
  (insert! coll (assoc el :_id (next-seq coll))))

(defn db-add-book [book]
  (with-mongo db
    (insert-with-id :books book)))

(defn db-get-books
  ([]
    (db-get-books {}))
  ([query]
    (with-mongo db
      (fetch :books :where query))))

(defn db-update-book [id book] 
  (with-mongo db
    (update! :books {:_id id} book)))

(defn db-delete-book [id]
  (with-mongo db
    (destroy! :books {:_id id})))