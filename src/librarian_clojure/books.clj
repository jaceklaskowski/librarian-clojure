(ns librarian-clojure.books
  (:use librarian-clojure.db
        [somnium.congomongo :only (object-id)]) ;; TODO: Move to librarian-clojure.db and remove
  (:require [hiccup.core :as hiccup]
            [hiccup.page-helpers :as hiccup-helpers]
            [hiccup.form-helpers :as form]
            [ring.util.response :as ring-util]))

(defn render-row [cells]
  [:tr (map (fn [v] [:td v]) cells)])

(defn prepare-row [book]
  [(:author book) 
   (:title book)
   [:a {:href (str "/books/edit?id=" (:_id book)) } "Edit"]
   [:a {:href (str "/books/delete?id=" (:_id book)) } "Delete"]
   ])

(defn render-book-table [books]
  [:table 
   (render-row ["Author" "Title"])
   (map #(-> % prepare-row render-row) books)])

(defn render-books [caption books]
  (hiccup/html
    [:h1 "Books in Database"]
    (render-book-table books)))

(defn render-book-form 
  ([] 
    (hiccup/html
      [:h1 "Enter Book"]
      (form/form-to [:post "/books"]
                    [:p (form/label :author "Author:") (form/text-field :author)]
                    [:p (form/label :title "Title:") (form/text-field :title)]
                    [:p (form/submit-button "Create")])))
  ([book]
    (hiccup/html
      [:h1 "Edit Book"]
      (form/form-to [:post "/books"]
                    (form/hidden-field :id (:_id book))
                    [:p (form/label :author "Author:") (form/text-field :author (:author book))]
                    [:p (form/label :title "Title:") (form/text-field :title (:title book))]
                    [:p (form/submit-button "Update")]))))

(defn get-books []
  (try
    (hiccup-helpers/html4
      (render-books "Books in Database" (db-get-books))
      (render-book-form))
    (catch com.mongodb.MongoException$Network e
        (hiccup/html
          [:h1 "MongoDB not configured"]))))
  
(defn post-books [id author title]
  (do
    (let [book {:author author :title title}]
      (if id
        (db-update-book id book) 
        (db-add-book book)))
    (ring-util/redirect "/books")))

(defn delete-book [id]
  (do
    (db-delete-book id)
    (ring-util/redirect "/books")))

(defn edit-book [id]
  (let [book (first (db-get-books {:_id (object-id id)}))]
     (hiccup-helpers/html4
       (render-books "Books in Database" (db-get-books))
       (render-book-form book))))
