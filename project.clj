;; http://corfield.org/blog/post.cfm/testing-your-project-against-multiple-versions-of-clojure
;; common dependencies
(def common-deps '[[ring/ring-jetty-adapter "1.0.1"]
                   [ring/ring-devel "1.0.1"]
                   [compojure "1.0.1"]
                   [hiccup "0.3.8"]
                   [congomongo "0.1.8"]
                   [midje "1.3.1"]
                   [org.clojure/data.json "0.1.2"]])

;; project definition for multi-version testing - consult :multi-deps option
(defproject librarian-clojure "0.0.1-SNAPSHOT"
  :description "Book manager in Clojure"
  :url "http://github.com/jaceklaskowski/librarian-clojure"
  :repositories [["sonatype-snapshots"
                  "https://oss.sonatype.org/content/repositories/snapshots/"]
                 ["stuart" "http://stuartsierra.com/maven2"]]
  :dependencies ~(conj common-deps
                       '[org.clojure/clojure "1.4.0-alpha3"])
  :repl-init librarian-clojure.repl
  :multi-deps {"1.4.0" ~(conj common-deps
                       '[org.clojure/clojure "1.4.0-master-SNAPSHOT"])}
  :dev-dependencies [[lein-multi "1.1.0"]
                     [lein-ring "0.5.4"]
                     [lein-eclipse "1.0.0"]
                     [ring-mock "0.1.1"]
                     [midje "1.3.1" :exclusions [org.clojure/clojure]]
                     [lein-midje "1.0.8"]
                     [com.stuartsierra/lazytest "1.2.3"]]
  :ring {:handler librarian-clojure.core/app
         :init librarian-clojure.run/run-local}
  :main librarian-clojure.run
  :run-aliases {:local librarian-clojure.run/run-local
                :heroku librarian-clojure.run/run-heroku}
  :aot :all)
