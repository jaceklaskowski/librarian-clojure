(ns librarian-clojure.frontend
  (:use [jayq.core :only [$]]
        [jayq.util :only [log]])
  (:require [jayq.core :as jq]))

(defn hello [x]
  (js/alert (str "Hello " x "!")))

(defn create-button
  [& {:keys [button-class icon caption callback]}]
  (let [button ($ "<button>")]
    (when button-class 
      (jq/add-class button (name button-class)))
    (when icon
      (jq/append button (-> ($ "<i>") (jq/add-class (name icon))))
      (jq/append button " "))
    (when caption
      (jq/append button caption))
    (when callback
      (jq/click button callback))
    button))
      
