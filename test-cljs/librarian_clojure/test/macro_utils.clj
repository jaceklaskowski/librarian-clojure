(ns librarian-clojure.test.macro-utils)

(defmacro deftest [name & body] `(js/test ~name (fn [] ~@body)))

