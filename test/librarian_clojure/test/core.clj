(ns librarian-clojure.test.core
  (:use librarian-clojure.core
        ring.mock.request
        midje.sweet)
  (:require [librarian-clojure.books :as books]
            [clojure.data.json :as json]))

(def book {:author "Robert C. Martin" :title "Clean Code"})

(fact "get /books -> list all books"
      (against-background (books/get-books) => "Get Books")
      (let [req (request :get "/books")
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Get Books")))

(fact "post /books/:id -> update existing"
      (against-background 
        (books/update-book "15" "Robert C. Martin" "Clean Code") => (str "Update as expected"))
      (let [req (request :post "/books/15" book)
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Update as expected"))
      (let [req (request :post "/books/" book)
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/"}))

(fact "put /books -> insert new"
      (against-background 
        (books/add-book "Robert C. Martin" "Clean Code") => (str "Insert as expected"))
      (let [req (request :put "/books" book)
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Insert as expected")))

(fact "delete /books/:id -> delete"
      (against-background 
        (books/delete-book "15") => (str "Delete as expected"))
      (let [req (request :delete "/books/15")
            response (app req)]
        (:status response) => 200
        (:body response) => (json/json-str "Delete as expected"))
      (let [req (request :delete "/books")
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/"}))