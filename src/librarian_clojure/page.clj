(ns librarian-clojure.page
  (:require (librarian-clojure [security :as sec]
                               [books :as books]))
  (:use [net.cgrand.enlive-html :as html :only (clone-for content nth-of-type)]))

(defn greet-user [{:keys [login]}]
  (when login
    (str "Hello, " login)))

(def ^:private remove-element nil)

(html/deftemplate index-for-user "public/index.html"
  [{:keys [books] :as ctxt}]
  [:a#enlive-hello-user] (content (get ctxt :hello-user ""))
  [:ul#enlive-login-create-account-buttons] remove-element
  [:.book-list :tr] (clone-for [{:keys [_id author title]} books]
                               [[:td (nth-of-type 1)]] (content (str _id))
                               [[:td (nth-of-type 2)]] (content author)
                               [[:td (nth-of-type 3)]] (content title)))

(html/deftemplate index-for-public "public/index.html"
  [books]
  [:ul#enlive-hello-user-area] remove-element
  [:th#enlive-user-actions-header] remove-element
  [:tr#enlive-user-actions-row] remove-element
  [:.book-list :tr] (clone-for [{:keys [_id author title]} books]
                               [[:td (nth-of-type 1)]] (content (str _id))
                               [[:td (nth-of-type 2)]] (content author)
                               [[:td (nth-of-type 3)]] (content title)
                               [[:td (nth-of-type 4)]] remove-element))

(defn render-user [request]
  (if-let [user (sec/get-user request)]
    (index-for-user {:hello-user (greet-user user)
                     :books (books/get-books)})
    (index-for-public (books/get-books))))

(html/deftemplate admin "public/admin.html"
  [ctxt]
  [:a#enlive-hello-admin] (content (get ctxt :hello-admin)))

(defn render-admin [request]
  (admin {:hello-admin (greet-user (sec/get-user request))}))

(defn render-main [request]
  (if (-> request (sec/has-role? :admin))
    (render-admin request)
    (render-user request)))
