(ns librarian-clojure.test.frontend
  (:use [jayq.core :only [$]])
  (:require [librarian-clojure.frontend :as frontend])
  (:require-macros [librarian-clojure.test.macro-utils :as m]))

(defn jqdom [jqnode]
  (.get jqnode 0))

(defn jqdom= [jqnode1 jqnode2]
  (.isEqualNode (jqdom jqnode1) (jqdom jqnode2)))

(m/deftest "(create-button) returns an empty button"
  (js/ok (jqdom= (frontend/create-button) ($ "<button>")), "Should return empty button"))

(m/deftest "(create-button :class :foo) returns button with class foo"
  (let [button (jqdom (frontend/create-button :button-class :foo))]
    (js/ok (== button/tagName "BUTTON") "This is not a button!")
    (js/ok (== button/className "foo") "Button does not have class foo!")))
