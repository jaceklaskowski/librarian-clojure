(ns librarian-clojure.page
  (:require [librarian-clojure.security :as sec]
            [net.cgrand.enlive-html :as html]))

(defn greet-user [{:keys [login]}]
  (when login
    (str "Hello, " login)))

(def ^:private remove-element nil)

(html/deftemplate index-for-user "public/index.html"
  [ctxt]
  [:a#enlive-hello-user] (html/content (get ctxt :hello-user ""))
  [:ul#enlive-login-create-account-buttons] remove-element)

(html/deftemplate index-for-public "public/index.html"
  [ctxt]
  [:ul#enlive-hello-user-area] remove-element)

(defn render-user [request]
  (if-let [user (sec/get-user request)]
    (index-for-user {:hello-user (greet-user user)})
    (index-for-public {})))

(html/deftemplate admin "public/admin.html"
  [ctxt]
  [:a#enlive-hello-admin] (html/content (get ctxt :hello-admin)))

(defn render-admin [request]
  (admin {:hello-admin (greet-user (sec/get-user request))}))

(defn render-main [request]
  (if (-> request (sec/has-role? :admin))
    (render-admin request)
    (render-user request)))
