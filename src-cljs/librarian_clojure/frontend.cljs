(ns librarian-clojure.frontend
  (:use [jayq.core :only [$]]))

(defn hello [x]
  (js/alert (str "Hello " x "!")))

(defn create-button
  [& {:keys [button-class]}]
  (let [button ($ "<button>")]
    (when button-class 
      (.addClass button (name button-class)))
    button))
      
