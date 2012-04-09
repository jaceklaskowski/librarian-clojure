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

(def jacek {:login "jacek" :password "secr3t" :roles #{:admin}})
(with-redefs-fn {#'librarian-clojure.db/db-get-user (fn [_] jacek)
                 #'librarian-clojure.security/check-password (fn [_ _] true)}
  #(librarian-clojure.security/log-in {:params {:login "jacek" :password "secr3t"}}))

