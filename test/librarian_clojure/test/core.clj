(ns librarian-clojure.test.core
  (:use librarian-clojure.core
        ring.mock.request
        midje.sweet)
  (:require [librarian-clojure.books :as books]))

(def book {:author "Robert C. Martin" :title "Clean Code"})

(fact "/books list"
      (against-background (books/get-books) => "Get Books")
      (let [req (request :get "/books")
            response (app req)]
        (:status response) => 200
        (:body response) => "Get Books"))

(fact "/books form submit - update existing"
      (against-background 
        (books/update-book "15" "Robert C. Martin" "Clean Code") => (str "Update as expected"))
      (let [req (request :post "/books/update/15" book)
            response (app req)]
        (:status response) => 200
        (:body response) => "Update as expected")
      (let [req (request :post "/books/update" book)
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/books"}))

(fact "/books form submit - insert new"
      (against-background 
        (books/add-book "Robert C. Martin" "Clean Code") => (str "Insert as expected"))
      (let [req (request :put "/books" book)
            response (app req)]
        (:status response) => 200
        (:body response) => "Insert as expected"))

(fact "/books delete"
      (against-background 
        (books/delete-book "15") => (str "Delete as expected"))
      (let [req (request :get "/books/delete/15")
            response (app req)]
        (:status response) => 200
        (:body response) => "Delete as expected")
      (let [req (request :get "/books/delete")
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/books"}))

(fact "/books edit"
      (against-background 
        (books/edit-book "15") => (str "Edit as expected"))
      (let [req (request :get "/books/edit/15")
            response (app req)]
        (:status response) => 200
        (:body response) => "Edit as expected")
      (let [req (request :get "/books/edit")
            response (app req)]
        (:status response) => 302
        (:headers response) => {"Location" "/books"}))
