(ns librarian-clojure.test.frontend
  (:use [jayq.core :only [$]])
  (:require [librarian-clojure.frontend :as frontend]
            [jayq.core :as jq]
            [clojure.string :as str])
  (:require-macros [librarian-clojure.test.macro-utils :as m]))

(defn jqdom [jqnode]
  (.get jqnode 0))

(defn jqdom= [jqnode1 jqnode2]
  (.isEqualNode (jqdom jqnode1) (jqdom jqnode2)))

(m/deftest "(create-button) returns an empty button"
  (js/ok (jqdom= (frontend/create-button) ($ "<button>")), "Returns empty button"))

(m/deftest "(create-button :button-class :foo) returns button with class foo"
  (let [button (jqdom (frontend/create-button :button-class :foo))]
    (js/ok (== button/tagName "BUTTON") "This is a button!")
    (js/ok (== button/className "foo") "Button does have class foo!")))

(m/deftest "(create-button :button-class :foo :caption \"Foo\" :icon :icon-class) returns according button
TODO: add callback test"
  (let [jbutton (frontend/create-button :button-class :foo :caption "Foo" :icon :icon-class)
        button (jqdom jbutton)]
    (js/ok (== button/tagName "BUTTON") "This is a button!")
    (js/ok (== button/className "foo") "Button does have class foo!")
    (js/ok (-> jbutton (jq/children "i") first $ (jq/has-class "icon-class")) "There's an icon!")
    (js/ok (->> jbutton jq/text (re-find #"Foo")))))
