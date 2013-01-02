(ns librarian-clojure.test.frontend
  (:use [jayq.core :only [$]])
  (:require [librarian-clojure.frontend :as frontend]
            [jayq.core :as jq])
  (:require-macros [librarian-clojure.test.macro-utils :as m]))

(defn jqdom [jqnode]
  (.get jqnode 0))

(defn jqdom= [jqnode1 jqnode2]
  (.isEqualNode (jqdom jqnode1) (jqdom jqnode2)))

(def ok js/ok)

(defn not-ok [assertion comment] (js/ok (not assertion) comment))

(m/deftest "(create-button) returns an empty button"
  (js/ok (jqdom= (frontend/create-button) ($ "<button>")), "Returns empty button"))

(m/deftest "(create-button :button-class :foo) returns button with class foo"
  (let [button (jqdom (frontend/create-button :button-class :foo))]
    (ok (== button/tagName "BUTTON") "This is a button!")
    (ok (== button/className "foo") "Button does have class foo!")))

(m/deftest "(create-button :button-class :foo :caption \"Foo\" :icon :icon-class) returns according button"
  (let [jbutton (frontend/create-button :button-class :foo :caption "Foo" :icon :icon-class)
        button (jqdom jbutton)]
    (ok (== button/tagName "BUTTON") "This is a button!")
    (ok (== button/className "foo") "Button does have class foo!")
    (ok (-> jbutton (jq/children "i") first $ (jq/has-class "icon-class")) "There's an icon!")
    (ok (->> jbutton jq/text (re-find #"Foo")) "There's a caption!")))

(m/deftest-async "(create-button :callback ...) test" 1
  (let [button (frontend/create-button :callback (fn [] (ok true "Called once!") (js/start)))]
    (jq/trigger button :click)))

(m/deftest "(existing-book? ...)"
  (ok (frontend/existing-book? (-> "<span>" $ (jq/attr "data-id" "foo"))) "True when has data-id")
  (not-ok (frontend/existing-book? ($ "<span>")) "False when not"))

(m/deftest "(get-edited-book ...) test"
  (let [row ($ "<div data-id=\"foo\"><input name=\"author\" value=\"bar\"><input name=\"title\" value=\"baz\"></div>")
        res (frontend/get-edited-book row)]
    (ok (== (:_id res) "foo") "Has :id")
    (ok (== (:author res) "bar") "Has :author")
    (ok (== (:title res) "baz")) "Has :title"))
