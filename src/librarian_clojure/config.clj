(ns librarian-clojure.config)

(def ^:dynamic *env* nil)

(def ^:static mongo-config
  {:local  :test
   ;; mongodb: URL scheme allows for authentication with no additional Mongo API calls
   ;; See http://api.mongodb.org/java/current/com/mongodb/MongoClientURI.html
   :heroku "mongodb://heroku:passw0rd@staff.mongohq.com:10056/app2623043"})