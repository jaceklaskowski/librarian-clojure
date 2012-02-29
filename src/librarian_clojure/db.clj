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

(defn db-add-book [book]
  (with-mongo db
    (insert! :books book)))

(defn db-get-books
  ([]
    (db-get-books {}))
  ([query]
    (with-mongo db
      (fetch :books :where query))))

(defn db-update-book [id book] 
  (with-mongo db
    (update! :books {:_id (object-id id)} book)))

(defn db-delete-book [id]
  (with-mongo db
    (destroy! :books {:_id (object-id id)})))
