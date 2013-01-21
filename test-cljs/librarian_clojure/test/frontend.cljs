(ns librarian-clojure.test.frontend
  (:use [jayq.core :only [$ remove]]
        [librarian-clojure.test.test-helpers :only [jqdom jqdom= ok not-ok mockjax
                                                    mockjax-clear]])
  (:require [librarian-clojure.frontend :as frontend]
            [jayq.core :as jq])
  (:use-macros [librarian-clojure.test.macro-utils :only [deftest deftest-async]]))

(deftest "(create-button) returns an empty button"
  (js/ok (jqdom= (frontend/create-button) ($ "<button>")), "Returns empty button"))

(deftest "(create-button :button-class :foo) returns button with class foo"
  (let [button (jqdom (frontend/create-button :button-class :foo))]
    (ok (== button/tagName "BUTTON") "This is a button!")
    (ok (== button/className "foo") "Button does have class foo!")))

(deftest "(create-button :button-class :foo :caption \"Foo\" :icon :icon-class) 
returns according button"
  (let [jbutton (frontend/create-button :button-class :foo :caption "Foo" :icon :icon-class)
        button (jqdom jbutton)]
    (ok (== button/tagName "BUTTON") "This is a button!")
    (ok (== button/className "foo") "Button does have class foo!")
    (ok (-> jbutton (jq/children "i") first $ (jq/has-class "icon-class")) "There's an icon!")
    (ok (->> jbutton jq/text (re-find #"Foo")) "There's a caption!")))

(deftest-async "(create-button :callback ...) test" 1
  (let [button (frontend/create-button :callback 
                                       (fn [] (ok true "Called once!") (js/start)))]
    (jq/trigger button :click)))

(deftest "(existing-book? ...)"
  (ok (frontend/existing-book? (-> "<span>" $ (jq/attr "data-id" "foo"))) 
      "True when has data-id")
  (not-ok (frontend/existing-book? ($ "<span>")) 
          "False when not"))

(deftest "(get-edited-book ...) test"
  (let [row ($ (str "<div data-id=\"foo\"><input name=\"author\" value=\"bar\">"
                    "<input name=\"title\" value=\"baz\"></div>"))
        res (frontend/get-edited-book row)]
    (ok (== (:_id res) "foo") "Has :id")
    (ok (== (:author res) "bar") "Has :author")
    (ok (== (:title res) "baz") "Has :title")))

(deftest "(validate-book-with-handler book handler-fn) validation tests"
  (not-ok (frontend/validate-book-with-handler {} #(ok (pos? (count %)) "Non-empty error.")) 
          "Validation fails with an empty book")
  (not-ok (frontend/validate-book-with-handler {:author "Deso"} #(ok (pos? (count %)) "Non-empty error."))
          "Validation fails with partially complete book")
  (ok (frontend/validate-book-with-handler {:author "Deso" :title "Caso"} #(ok (zero? (count %))))
      "Validation succeeds with complete book"))
