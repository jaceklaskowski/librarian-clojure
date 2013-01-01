(ns librarian-clojure.test.frontend
  (:require [librarian-clojure.frontend :as frontend]))

(js/test "adder makes 2 from 1 and 1"
         (fn [] (js/ok (== (frontend/adder 1 1) 2) "Passed!")))
