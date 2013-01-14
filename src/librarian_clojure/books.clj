(ns librarian-clojure.books
  (:require [librarian-clojure.db :refer (db-get-books 
                                          db-add-book 
                                          db-update-book 
                                          db-get-book 
                                          db-delete-book)]))

(defn get-books []
  (db-get-books))

(defn get-book [id]
  (let [id (Integer/parseInt id)]
    (db-get-book id)))

(defn add-book [author title]
  (db-add-book {:author author :title title}))

(defn update-book [id author title]
  (let [id (Integer/parseInt id)]
    (db-update-book id {:author author :title title})
    (db-get-book id)))

(defn delete-book [id]
  (let [id (Integer/parseInt id)]
    (db-delete-book id)
    {:book-deleted id}))
