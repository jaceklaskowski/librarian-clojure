(ns librarian-clojure.config)

(def ^:dynamic *env* nil)

(def ^:static mongo-config
  {:local  :test
   ;; MONGOHQ_URL should be set on Heroku
   :heroku (or (System/getenv "MONGOHQ_URL")
               ;; hack to force meaningful exception when outside Heroku
               "mongodb://MONGOHQ_URL not set. Perhaps you run the app outside Heroku?")})
