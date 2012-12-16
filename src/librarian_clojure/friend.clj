(ns librarian-clojure.friend
  (:use [cemerick.friend.util :only (gets)])
  (:require [cemerick.friend [workflows :as workflows]]))

(defn custom-interactive-form
  [& {:keys [login-uri username-field password-field]
      :or {username-field :username password-field :password}
      :as form-config}]
  (let [form-fn (apply workflows/interactive-form (mapcat identity form-config))]
    (fn [{:keys [uri request-method params] :as request}]
      (when (and (gets :login-uri form-config (:com.cemerick.friend/auth-config request))
                 (= :post request-method))
        (let [[username password] ((juxt username-field password-field) params)]
          (form-fn (-> request
                       (assoc-in [:params :username] username)
                       (assoc-in [:params :password] password))))))))







