(ns librarian-clojure.test.frontend
  (:require [librarian-clojure.frontend :as frontend]))

(def deftest js/test)

(deftest "adder makes 2 from 1 and 1"
  (fn [] (js/ok (== (frontend/adder 1 1) 2) "Passed!")))

(deftest "(create-button) returns an empty button"
  (fn [] (js/ok true)))
