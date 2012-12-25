(ns librarian-clojure.friend
  (:use [cemerick.friend.util :only (gets)])
  (:require [librarian-clojure.security :as security]
            [clojure.data.json :as json]
            [cemerick.friend :as friend]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]))

(defn signup-form
  [{:keys [login password]}]
  (let [success-fn (fn [{:keys [username roles] :as new-user}]
                     (-> {:identity username :username username
                          :login username :roles roles}
                         (with-meta {:cemerick.friend/redirect-on-auth? false})
                         workflows/make-auth))]
    (security/signup-workflow-handler login password success-fn)))

(defn signup-workflow [{:keys [uri request-method params]}]
  (when (and (= uri "/signup")
             (= request-method :post))
    (let [result (signup-form params)]
      (if (friend/auth? result)
        result
        {:body (json/json-str result)}))))

(defn custom-interactive-form
  [& {:keys [login-uri username-field password-field]
      :or {username-field :username password-field :password}
      :as form-config}]
  (let [form-fn (apply workflows/interactive-form (mapcat identity form-config))]
    (fn [{:keys [uri request-method params] :as request}]
      (when (and (= (gets :login-uri form-config (:cemerick.friend/auth-config request)) uri)
                 (= :post request-method))
        (let [[username password] ((juxt username-field password-field) params)]
          (form-fn (-> request
                       (assoc-in [:params :username] username)
                       (assoc-in [:params :password] password))))))))

