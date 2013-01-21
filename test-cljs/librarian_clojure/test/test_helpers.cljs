(ns librarian-clojure.test.test-helpers
  (:use [jayq.core :only [$ ->ajax-settings]])
  (:require-macros [librarian-clojure.test.macro-utils :as m]))

(defn jqdom [jqnode]
  (.get jqnode 0))

(defn jqdom= [jqnode1 jqnode2]
  (.isEqualNode (jqdom jqnode1) (jqdom jqnode2)))

(def ok js/ok)

(defn not-ok [assertion comment] (js/ok (not assertion) comment))

(defn mockjax [& {:keys [url type dataType responseText] :as settings}]
  (.mockjax js/jQuery (->ajax-settings settings)))

(defn mockjax-clear []
  (.mockjaxClear js/jQuery))
