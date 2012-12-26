(ns librarian-clojure.test.t-page
  (:use librarian-clojure.page
        librarian-clojure.security
        midje.sweet))

(fact "render-main renders admin page when authenticated as admin"
  (let [request {}]
    (render-main request) => "admin"
    (provided
      (has-role? request :admin) => true
      (render-admin request) => "admin")))

(fact "render-main renders user page for non-admin users"
  (let [request {}]
    (render-main request) => "user"
    (provided
      (has-role? request :admin) => false
      (render-user request) => "user")))
