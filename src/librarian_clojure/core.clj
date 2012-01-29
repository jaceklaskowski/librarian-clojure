(ns librarian-clojure.core
  (:use ring.adapter.jetty
        ring.handler.dump))

(defn hello-world [app]
  (fn [req]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body "Hello, world"}))

(defn upper-case [app]
  (fn [req]
    (let [resp (app req)
          body (:body resp)
          upper-case-body (.toUpperCase body)]
      (conj resp {:body upper-case-body}))))
  
(def app
  (-> handle-dump
    hello-world
    upper-case))

(defn -main [port]
  (run-jetty app {:port (Integer. port)}))
