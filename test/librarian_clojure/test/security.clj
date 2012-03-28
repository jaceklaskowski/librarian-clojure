(ns librarian-clojure.test.security
  (:use librarian-clojure.security
        ring.mock.request
        midje.sweet)
  (:require [librarian-clojure.db :as db]))

(def user {:login "admin" :password (crypt-password "adminPass") :roles [:admin :user]})
(def user {:login "user" :password (crypt-password "userPass") :roles [:user]})

(fact "Crypt and check passwords"
      (let [crypt (crypt-password "TestPassword")]
        (check-password "TestPassword" crypt) => true
        (check-password "TestPassworD" crypt) => false))

