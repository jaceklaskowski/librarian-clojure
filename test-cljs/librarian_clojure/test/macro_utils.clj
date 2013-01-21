(ns librarian-clojure.test.macro-utils)

(defmacro deftest [name & body] `(js/test ~name (fn [] ~@body)))

(defmacro deftest-async 
  ([name expected & body] `(js/asyncTest ~name ~expected (fn [] ~@body))))

(defmacro redef-fn [fn-binding fn-val & body]
  (let [old-binding (-> fn-binding name gensym)] 
    `(try
       ~(list 'set! fn-binding fn-val)
       ~@body
       (finally 
         ~(list 'set! fn-binding old-binding)))))
