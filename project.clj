;; http://corfield.org/blog/post.cfm/testing-your-project-against-multiple-versions-of-clojure
;; common dependencies
(def common-deps '[[ring/ring-jetty-adapter "1.1.6" :exclusions [org.clojure/clojure]]
                   [ring/ring-devel "1.1.6" :exclusions [org.clojure/clojure]]
                   [compojure "1.1.3" :exclusions [org.clojure/clojure
                                                   ring/ring-core]]
                   [hiccup "1.0.2" :exclusions [org.clojure/clojure]] 
                   [congomongo "0.3.3" :exclusions [org.clojure/clojure]]
                   [org.clojure/data.json "0.2.1" :exclusions [org.clojure/clojure]]
                   [jbcrypt "0.3" :exclusions [org.clojure/clojure]]
                   [com.cemerick/friend "0.1.2" :exclusions [org.clojure/clojure]]])

(defproject librarian-clojure "0.0.1-SNAPSHOT"
  :description "Book manager in Clojure"
  :url "http://github.com/jaceklaskowski/librarian-clojure"
  :repositories [["sonatype-snapshots"
                  "http://oss.sonatype.org/content/repositories/snapshots/"]
                 ["stuart" "http://stuartsierra.com/maven2"]
                 ["stuart-snapshots" "http://stuartsierra.com/m2snapshots"]]
  :dependencies ~(conj common-deps
                       '[org.clojure/clojure "1.4.0"])
  :repl-init librarian-clojure.repl
  :dev-dependencies [[lein-multi "1.1.0" :exclusions [org.clojure/clojure]]
                     [lein-ring "0.6.4" :exclusions [org.clojure/clojure
                                                     hiccup]]
                     [lein-eclipse "1.0.0" :exclusions [org.clojure/clojure]]
                     [ring-mock "0.1.1" :exclusions [org.clojure/clojure
                                                     hiccup]]
                     [midje "1.3.1" :exclusions [org.clojure/clojure]]
                     [lein-midje "1.0.9" :exclusions [org.clojure/clojure]]
                     [com.stuartsierra/lazytest "1.2.3" :exclusions [org.clojure/clojure]]]
  :ring {:handler librarian-clojure.core/app
         :init librarian-clojure.run/run-local-ring}
  :main "librarian-clojure.run"
  :min-lein-version "2.0.0"
  :aot :all
  :profiles {;; https://devcenter.heroku.com/articles/clojure-support#runtime-behavior
             :production {:misc "configuration"
                          :main "librarian-clojure.run/run-heroku"
                          :mirrors {#"central|clojars"
                                    "http://s3pository.herokuapp.com/clojure"}}
             :dev {:main "librarian-clojure.run/run-local"
                   :dependencies [[ring-mock "0.1.1" :exclusions [org.clojure/clojure
                                                                  hiccup]]
                                  [midje "1.5-alpha3" :exclusions [org.clojure/clojure]]
                                  [lein-midje "2.0.3" :exclusions [org.clojure/clojure]]
                                  [com.stuartsierra/lazytest "1.2.3" :exclusions [org.clojure/clojure]]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.0-master-SNAPSHOT"]]}}
  :aliases {"run-local" ["with-profile" "dev" "run"]
            "all" ["with-profile" "dev,1.5"]}
  :warn-on-reflection true)
