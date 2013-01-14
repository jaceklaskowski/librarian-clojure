(ns librarian-clojure.page
  (:require [librarian-clojure (security :as sec)
                               (books :as books)]
            [net.cgrand.enlive-html :refer (clone-for content nth-of-type) :as html]))

(defn greet-user [{:keys [login]}]
  (when login
    (str "Hello, " login)))

(def ^:private remove-element nil)

(html/deftemplate index-for-user "public/index.html"
  [{:keys [books user]}]
  [:a#enlive-hello-user] (content (greet-user user))
  [:ul#enlive-login-create-account-buttons] remove-element
  [:div#enlive-hero-unit] remove-element
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
                               [[:td (nth-of-type 3)] [:a]] (html/do->
                                                              (content title)
                                                              (html/set-attr :href (str "/books/" _id)))
                               [[:td (nth-of-type 4)]] remove-element))

(defn render-user [request]
  (if-let [user (-> request sec/get-user)]
    (index-for-user {:user user
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

(html/deftemplate book-page-for-public "public/book.html"
  [book-id]
  [:ul#enlive-hello-user-area] remove-element)

(html/deftemplate book-page-for-user "public/book.html"
  [{:keys [book-id user]}]
  [:a#enlive-hello-user] (content (greet-user user))
  [:ul#enlive-login-create-account-buttons] remove-element)

(defn render-book-page [request book-id]
  (if-let [user (-> request sec/get-user)]
    (book-page-for-user {:book-id book-id
                         :user user})
    (book-page-for-public book-id)))
