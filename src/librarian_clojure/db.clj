(ns librarian-clojure.db
  (:use somnium.congomongo))

(declare db-add-book)

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
                       {:host "127.0.0.1" :port 27017})))
  ;; FIXME Remove after the delete action works - we know the ids
  (try
    (db-add-book {:_id 0 :author "Mickiewicz" :title "Konrad Wallenrod"})
  (catch Exception e)))

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
  ;; FIXME: Remove after db-add-book FIXME is removed
  (let [_id (if (= id "0") id (object-id id))]
    (with-mongo db
      (destroy! :books {:_id _id}))))
