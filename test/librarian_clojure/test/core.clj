(ns librarian-clojure.test.core
  (:use librarian-clojure.core
        midje.sweet)
  (:require [librarian-clojure.books :as books]
            [clojure.data.json :as json]
            [ring.mock.request :as ring]
            [cemerick.friend :as friend]))

(def book {:author "Robert C. Martin" :title "Clean Code"})

(fact "get /books -> list all books"
  (let [req (ring/request :get "/books")
        res (app req)]
    (:status res) => 200
    (:body res) => (json/json-str "Get Books"))
  (against-background
    (books/get-books) => "Get Books"))

(fact "post /books/:id -> update existing"
  (let [req (ring/request :post "/books/15" book)
        res (app req)]
    (:status res) => 200
    (:body res) => (json/json-str "Update as expected"))
  (let [req (ring/request :post "/books/" book)
        res (app req)]
    (:status res) => 302
    (:headers res) => {"Location" "/"})
  (against-background
    (friend/authorized? anything anything) => true
    (books/update-book "15" "Robert C. Martin" "Clean Code") => "Update as expected"))

(fact "put /books -> insert new"
  (let [req (ring/request :put "/books" book)
        response (app req)]
    (:status response) => 200
    (:body response) => (json/json-str "Insert as expected"))
  (against-background 
    (friend/authorized? anything anything) => true
    (books/add-book "Robert C. Martin" "Clean Code") => (str "Insert as expected")))

(fact "delete /books/:id -> delete"
  (let [req (ring/request :delete "/books/15")
        response (app req)]
    (:status response) => 200
    (:body response) => (json/json-str "Delete as expected"))
  (let [req (ring/request :delete "/books")
        response (app req)]
    (:status response) => 302
    (:headers response) => {"Location" "/"})
  (against-background 
    (friend/authorized? anything anything) => true
    (books/delete-book "15") => (str "Delete as expected")))
