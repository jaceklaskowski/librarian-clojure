(ns librarian-clojure.frontend
  (:use [jayq.core :only [$]]))

(defn hello [x]
  (js/alert (str "Hello " x "!")))

(defn adder [x y]
  (+ x y))

(hello "Adrian")
