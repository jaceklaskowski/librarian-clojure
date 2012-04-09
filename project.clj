;; http://corfield.org/blog/post.cfm/testing-your-project-against-multiple-versions-of-clojure
;; common dependencies
(def common-deps '[[ring/ring-jetty-adapter "1.1.0-beta1" :exclusions [org.clojure/clojure]]
                   [ring/ring-devel "1.1.0-beta1" :exclusions [org.clojure/clojure]]
                   [compojure "1.0.1" :exclusions [org.clojure/clojure
                                                   ring/ring-core]]
                   [hiccup "0.3.8" :exclusions [org.clojure/clojure]]
                   [congomongo "0.1.8" :exclusions [org.clojure/clojure]]
                   [midje "1.3.1" :exclusions [org.clojure/clojure]]
                   [org.clojure/data.json "0.1.2" :exclusions [org.clojure/clojure]]
                   [jbcrypt "0.3" :exclusions [org.clojure/clojure]]
                   [sandbar "0.4.0-SNAPSHOT" :exclusions [org.clojure/clojure]]])

;; project definition for multi-version testing - consult :multi-deps option
(defproject librarian-clojure "0.0.1-SNAPSHOT"
  :description "Book manager in Clojure"
  :url "http://github.com/jaceklaskowski/librarian-clojure"
  :repositories [["sonatype-snapshots"
                  "http://oss.sonatype.org/content/repositories/snapshots/"]
                 ["stuart" "http://stuartsierra.com/maven2"]]
  :dependencies ~(conj common-deps
                       ; KG 1.4.0 does not seem to work with sandbar
                       ; '[org.clojure/clojure "1.4.0-alpha3"])
                       '[org.clojure/clojure "1.3.0"])
  :repl-init librarian-clojure.repl
  :multi-deps {"1.4.0" ~(conj common-deps
                       '[org.clojure/clojure "1.4.0-master-SNAPSHOT"])}
  :dev-dependencies [[lein-multi "1.1.0" :exclusions [org.clojure/clojure]]
                     [lein-ring "0.5.4" :exclusions [org.clojure/clojure]]
                     [lein-eclipse "1.0.0" :exclusions [org.clojure/clojure]]
                     [ring-mock "0.1.1" :exclusions [org.clojure/clojure]]
                     [midje "1.3.1" :exclusions [org.clojure/clojure]]
                     [lein-midje "1.0.8" :exclusions [org.clojure/clojure]]
                     [com.stuartsierra/lazytest "1.2.3" :exclusions [org.clojure/clojure]]]
  :ring {:handler librarian-clojure.core/app
         :init librarian-clojure.run/run-local-ring}
  :main librarian-clojure.run
  :run-aliases {:local librarian-clojure.run/run-local
                :heroku librarian-clojure.run/run-heroku}
  :aot :all
  :profiles {:test {:dependencies [[ring-mock "0.1.1" :exclusions [org.clojure/clojure]]]}})
