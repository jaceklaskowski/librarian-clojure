(ns librarian-clojure.test.macro-utils)

(defmacro deftest [name & body] `(js/test ~name (fn [] ~@body)))

(defmacro deftest-async 
  ([name expected & body] `(js/asyncTest ~name ~expected (fn [] ~@body))))

