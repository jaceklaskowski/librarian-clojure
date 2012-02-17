(ns librarian-clojure.books
  (:require [somnium.congomongo :as congo]
            [hiccup.core :as hiccup]
            [hiccup.page-helpers :as hiccup-helpers]
            [hiccup.form-helpers :as form]
            [ring.util.response :as ring-util]))

;;;;;; Database

(def conn 
  (congo/make-connection "test"
                   :host "127.0.0.1"
                   :port 27017))

; Global !
(congo/set-connection! conn)

(defn db-add-book [book] (congo/insert! :books book))

(defn db-get-books
  ([] (db-get-books {}))
  ([query] (congo/fetch :books :where query)))

(defn db-update-book [id book] (congo/update! :books {:_id (congo/object-id id)} book))

(defn db-delete-book [id] (congo/destroy! :books {:_id (congo/object-id id)}))

;;;;;; View/logic

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
  (hiccup-helpers/html4
    (render-books "Books in Database" (db-get-books))
    (render-book-form)))

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
  (let [book (first (db-get-books {:_id (congo/object-id id)}))]
     (hiccup-helpers/html4
       (render-books "Books in Database" (db-get-books))
       (render-book-form book))))
