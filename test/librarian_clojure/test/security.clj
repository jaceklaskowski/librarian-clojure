(ns librarian-clojure.test.security
  (:use librarian-clojure.security
        ring.mock.request
        midje.sweet
        [ring.util.response :only (redirect)])
  (:require [librarian-clojure.db :as db]))

(def user {:login "admin" :password (crypt-password "adminPass") :roles [:admin :user]})
(def user {:login "user" :password (crypt-password "userPass") :roles [:user]})

(fact "Crypt and check passwords"
      (let [crypt (crypt-password "TestPassword")]
        (check-password "TestPassword" crypt) => true
        (check-password "TestPassworD" crypt) => false))

(defn handler [request] {:body "Hello, world"})

(fact "Security with no policy"
      (let [policy []
            app ((wrap-security policy) handler)
            resp (app {:uri "/admin"})]
        (:body resp) => "Hello, world"))

(fact "Security with policy and match on role"
      (against-background (get-user) => {:login "Admin" :roles #{:admin}})
      (let [policy [[#"/admin.*" :admin]]
            app ((wrap-security policy) handler)
            resp (app {:uri "/admin"})]
        (:body resp) => "Hello, world"))

(fact "Security with policy on different URL"
      (against-background (get-user) => {:login "Admin" :roles #{:poorguy}})
      (let [policy [[#"/admin.*" :admin]]
            app ((wrap-security policy) handler)
            resp (app {:uri "/poorguys"})]
        (:body resp) => "Hello, world"))

(fact "Security with policy and no match"
      (against-background (get-user) => {:login "Admin" :roles #{:poorguy}})
      (let [policy [[#"/admin.*" :admin]]
            app ((wrap-security policy) handler)
            resp (app {:uri "/admin"})]
        resp => (redirect "/permission-denied")))

