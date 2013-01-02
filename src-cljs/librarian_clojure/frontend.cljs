(ns librarian-clojure.frontend
  (:use [jayq.core :only [$]]
        [jayq.util :only [log]])
  (:require [jayq.core :as jq]
            [clojure.string :as str]))

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
      (jq/on button :click callback))
    button))
      
(defn existing-book?
  [row]
  (jq/attr row "data-id"))

(defn get-edited-book
  [row]
  {:_id (jq/attr row "data-id")
   :author (-> row (jq/find "input[name=author]") jq/val str/trim)
   :title (-> row (jq/find "input[name=title]") jq/val str/trim)})
