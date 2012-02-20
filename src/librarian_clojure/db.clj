(ns librarian-clojure.db
  (:use somnium.congomongo))

(def conn 
  (make-connection :app2623043
                   {:host "127.0.0.1" :port 27017}
                   {:host "staff.mongohq.com" :port 10056}))

(defn db-add-book [book]
  (with-mongo conn
    (insert! :books book)))

(defn db-get-books
  ([]
    (db-get-books {}))
  ([query]
    (with-mongo conn
      (fetch :books :where query))))

(defn db-update-book [id book] 
  (with-mongo conn
    (update! :books {:_id (object-id id)} book)))

(defn db-delete-book [id] 
  (with-mongo conn
    (destroy! :books {:_id (object-id id)})))
