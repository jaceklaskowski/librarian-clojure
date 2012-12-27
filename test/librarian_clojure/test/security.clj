(ns librarian-clojure.test.security
  (:use librarian-clojure.security
        midje.sweet)
  (:require [librarian-clojure.db :as db]
            [ring.mock.request :as ring]))

(fact "get-user-by-login username contract"
  (get-user-by-login "admin") => (contains {:username "admin"})
  (provided (db/db-get-user "admin") => {:login "admin"}))
