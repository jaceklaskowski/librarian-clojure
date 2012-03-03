(ns librarian-clojure.books
  (:use librarian-clojure.db
        [somnium.congomongo :only (object-id)]) ;; TODO: Move to librarian-clojure.db and remove
  (:require [hiccup.core :as hiccup]
            [hiccup.page-helpers :as hiccup-helpers]
            [hiccup.form-helpers :as form]
            [ring.util.response :as ring-util]
            [clojure.data.json :as json]))

(defn render-row [cells]
  [:tr (map (fn [v] [:td v]) cells)])

(defn prepare-row [book]
  [(:author book) 
   (:title book)
   [:a {:href (str "/books/edit/" (:_id book)) } "Edit"]
   [:a {:href (str "/books/delete/" (:_id book)) } "Delete"]
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
      (form/form-to [:post "/books/add"]
                    [:p (form/label :author "Author:") (form/text-field :author)]
                    [:p (form/label :title "Title:") (form/text-field :title)]
                    [:p (form/submit-button "Create")])))
  ([book]
    (hiccup/html
      [:h1 "Edit Book"]
      (form/form-to [:post (str "/books/update/" (:_id book))]
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

;; https://github.com/clojure/data.json/blob/master/src/main/clojure/clojure/data/json.clj
(defn- write-json-mongodb-objectid [x out escape-unicode?]
  (json/write-json (str x) out escape-unicode?))

(extend org.bson.types.ObjectId json/Write-JSON
  {:write-json write-json-mongodb-objectid})

(defn get-books-json []
  (let [books (db-get-books)
        books-json (json/json-str books)]
    ;;(println "RESULT: " books-json)
    books-json))

(defn add-book [author title]
  (let [saved-book (db-add-book {:author author :title title})]
    (json/json-str saved-book)))

(defn update-book [id author title]
  (do
    (db-update-book (Integer. id) {:author author :title title})
    (ring-util/redirect "/")))

(defn delete-book [id]
  (do
    (db-delete-book (Integer. id))
    (ring-util/redirect "/")))

(defn edit-book [id]
  (let [book (first (db-get-books {:_id (Integer. id)}))]
     (hiccup-helpers/html4
       (render-books "Books in Database" (db-get-books))
       (render-book-form book))))
