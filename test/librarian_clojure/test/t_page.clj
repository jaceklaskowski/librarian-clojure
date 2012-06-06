(ns librarian-clojure.test.t-page
  (:use librarian-clojure.page
        librarian-clojure.security
        midje.sweet))

(fact "render-main renders admin page"
      (against-background (has-role? :admin) => true
                          (render-admin) => "admin")
      (render-main) => "admin")

(fact "render-main renders user page"
      (against-background (has-role? :admin) => false
                          (render-user) => "user")
      (render-main) => "user")
