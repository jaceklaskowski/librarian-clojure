(ns librarian-clojure.test.core
  (:use librarian-clojure.core
        midje.sweet)
  (:require [librarian-clojure.books :as books]
            [clojure.data.json :as json]
            [ring.mock.request :as ring]))

(def book {:author "Robert C. Martin" :title "Clean Code"})

(future-fact "get /books -> list all books"
  (let [req (ring/request :get "/books")
        res (app req)
        _ (println "+++ response" res)]
    (:status res) => 200
    (:body res) => (json/json-str "Get Books")
    (provided
      (books/get-books) => "Get Books")))

(future-fact "post /books/:id -> update existing"
      (let [req (ring/request :post "/books/15" book)
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Update as expected"))
      (let [req (ring/request :post "/books/" book)
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/"})
      (provided
        (books/update-book "15" "Robert C. Martin" "Clean Code") => "Update as expected"))

(future-fact "put /books -> insert new"
      (against-background 
        (books/add-book "Robert C. Martin" "Clean Code") => (str "Insert as expected"))
      (let [req (ring/request :put "/books" book)
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Insert as expected")))

(future-fact "delete /books/:id -> delete"
  (let [req (ring/request :delete "/books/15")
        response (app req)]
    (:status response) => 200
    (:body response) => (json/json-str "Delete as expected"))
  (let [req (ring/request :delete "/books")
        response (app req)]
    (:status response) => 302
    (:headers response) => {"Location" "/"}))
