(ns librarian-clojure.frontend
  (:use [jayq.core :only [$]]
        [jayq.util :only [log]])
  (:require [jayq.core :as jq]
            [clojure.string :as str]))

(defn create-button
  [& {:keys [button-class icon caption callback]}]
  (let [button ($ "<button>")]
    (when button-class 
      (jq/add-class button (name button-class)))
    (when icon
      (jq/append button (-> ($ "<i>") (jq/add-class (name icon))))
      (jq/append button " "))
    (when caption
      (jq/append button caption))
    (when callback
      (jq/on button :click callback))
    button))
      
(defn book-id-from-row [row]
  (jq/attr row "data-id"))

(defn existing-book?
  [row]
  (book-id-from-row row))

(defn get-edited-book
  [row]
  {:_id (jq/attr row "data-id")
   :author (-> row (jq/find "input[name=author]") jq/val str/trim)
   :title (-> row (jq/find "input[name=title]") jq/val str/trim)})

(defn delete-book 
  [row]
  (let [book-id (jq/attr row "data-id")] 
    (jq/ajax (str "/books/" book-id)
             {:type "DELETE"
              :dataType :json
              :success (fn [book]
                         (jq/remove row))})))

(defn validate-book-with-handler [book handler-fn]
  (let [error ""
        error (if-not (-> book :author count pos?) 
                (str error "Book has not author\n") 
                error)
        error (if-not (-> book :title count pos?)
                (str error "Book has no title\n")
                error)]
    (handler-fn error)
    (-> error count zero?)))

(defn validate-book [book]
  (validate-book-with-handler book #(when (-> % count pos?) (js/alert %))))

(defn review-book 
  "FIXME: Implement me!"
  [row])

(defn delete-book [row]
  (let [book-id (book-id-from-row row)]
    (jq/ajax (str "/books/" book-id)
             {:type "DELETE"
              :dataType :json
              :success (fn [book]
                         (jq/remove row))})))

(declare append-book)
(declare append-book-admin)

(defn perform-save
  [book row]
  (jq/ajax "/books"
           {:type "PUT"
            :dataType :json
            :success (fn [book]
                       (jq/empty row)
                       (append-book row book))}))

(defn perform-update
  [row]
  (jq/ajax "/books"
           {:type "PUT"
            :dataType :json
            :success (fn [book]
                       (jq/empty row)
                       (append-book row book))}))

(defn update-book [row]
  (let [book (get-edited-book row)]
    (if (validate-book book)
      (-> row (jq/find "input") (jq/attr "disabled" true))
      (-> row (jq/find "button") (jq/attr "disabled" true))
      (if (existing-book? row)
        (perform-update book row)
        (perform-save book row)))))

(defn cancel-edit [row]
  (if (existing-book? row)
    (let [book {:_id (jq/attr row "data-id")
                :author (jq/attr row "data-author")
                :title (jq/attr row "data-title")}]
      (jq/empty row)
      (append-book-admin row book))))

(defn edit-book [row]
  (jq/empty row)
  (if (existing-book? row)
    (-> row
        (jq/append (str "<td>" (jq/attr row "data-id") "</td>"))
        (jq/append (str "<td><input type=\"text\" name=\"author\" value=\"" 
                        (jq/attr row "data-author") "\" /></td>"))
        (jq/append (str "<td><input type=\"text\" name=\"title\" value=\"" (jq/attr row "data-title") "\" /></td>")))
    (-> row
        (jq/append "<td></td>")
        (jq/append "<td><input type=\"text\" name=\"author\" placeholder=\"Author\" /></td>")
        (jq/append "<td><input type=\"text\" name=\"title\" placeholder=\"Title\" /></td>")))
  (let [save-button (create-button :button-class "btn btn-primary" 
                                   :caption "Save"
                                   :icon "icon-ok icon-white"
                                   :callback (fn [] (update-book row)))
        cancel-button (create-button :button-class "btn"
                                     :caption "Cancel"
                                     :callback (fn [] (cancel-edit row)))
        buttons (-> ($ "<td>") 
                    (jq/append save-button)
                    (jq/append " ")
                    (jq/append cancel-button))]
    (-> row (jq/append buttons) (jq/find "input:first") jq/focus)))

(defn new-book-clicked []
  (let [row ($ "<tr>")]
    (jq/append ($ ".book-list") row)
    (edit-book row)))

(defn append-book [row {:keys [_id author title] :as book}]
  (-> row
      (jq/attr "data-id" _id)
      (jq/attr "data-author" author)
      (jq/attr "data-title" title)
      (jq/append (str "<td>" _id "</td>"))
      (jq/append (str "<td>" author "</td>"))
      (jq/append (str "<td>" title "</td>"))))

(defn append-book-admin [row book]
  (append-book row book)
  (let [edit-button (create-button :button-class "btn btn-primary"
                                   :icon "icon-edit icon-white"
                                   :caption "Edit"
                                   :callback (fn [] (edit-book row)))
        delete-button (create-button :button-class "btn btn-danger"
                                     :icon "icon-trash icon-white"
                                     :caption "Delete"
                                     :callback (fn [] (delete-book row)))
        review-button (create-button :button-class "btn"
                                     :icon "icon-star"
                                     :caption "Review"
                                     :callback (fn [] (review-book row)))
        buttons (-> ($ "<td>")
                    (jq/append edit-button)
                    (jq/append " ")
                    (jq/append review-button)
                    (jq/append " ")
                    (jq/append delete-button))]
    (jq/append row buttons)))

