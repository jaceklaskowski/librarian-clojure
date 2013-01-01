(defproject librarian-clojure "0.0.1-SNAPSHOT"
  :description "Book manager in Clojure"
  :url "http://github.com/jaceklaskowski/librarian-clojure"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"}
  :dependencies [[ring/ring-jetty-adapter "1.1.6" :exclusions [org.clojure/clojure]]
                 [ring/ring-devel         "1.1.6" :exclusions [org.clojure/clojure]]
                 [compojure               "1.1.3" :exclusions [org.clojure/clojure
                                                               ring/ring-core]]
                 [hiccup                  "1.0.2" :exclusions [org.clojure/clojure]]
                 [congomongo              "0.3.3" :exclusions [org.clojure/clojure]]
                 [org.clojure/data.json   "0.2.1" :exclusions [org.clojure/clojure]]
                 [jbcrypt                 "0.3"   :exclusions [org.clojure/clojure]]
                 [com.cemerick/friend     "0.1.2" :exclusions [org.clojure/clojure]]
                 [lib-noir                "0.3.1" :exclusions [org.clojure/clojure]]
                 [enlive                  "1.0.1" :exclusions [org.clojure/clojure]]
                 [org.clojure/clojure     "1.4.0"]]
  :repl-init librarian-clojure.repl
  :ring {:handler librarian-clojure.core/app
         :init librarian-clojure.run/run-local-ring}
  :main librarian-clojure.run
  :profiles {;; https://devcenter.heroku.com/articles/clojure-support#runtime-behavior
             :production {:misc "configuration"
                          :main librarian-clojure.run/run-heroku
                          :mirrors {#"central|clojars"
                                    "http://s3pository.herokuapp.com/clojure"}}
             :dev {:main librarian-clojure.run/run-local
                   :dependencies [[ring-mock                 "0.1.1" :exclusions [org.clojure/clojure
                                                                                  hiccup]]
                                  [midje                     "1.5-alpha3" :exclusions [org.clojure/clojure]]
                                  [com.stuartsierra/lazytest "1.2.3" :exclusions [org.clojure/clojure]]
                                  [jayq "2.0.0"]]
                   :plugins [[lein-midje "2.0.3"]
                             [lein-cljsbuild "0.2.10"]]
                   :hooks [leiningen.cljsbuild]
                   :cljsbuild {:builds {:main {:source-path "src-cljs"
                                               :compiler {:output-to "resources/public/js/librarian-cljs.js"
                                                          :optimizations :simple
                                                          :pretty-print true}
                                               :jar true}
                                        :test {:source-path "test-cljs"
                                               :compiler {:output-to "resources/public/tests/js/librarian-tests.js"
                                                          :optimizations :simple
                                                          :pretty-print true}
                                               :jar true}}}
                   :repl-options {:init-ns librarian-clojure.repl}}
             ;; FIXME deps copied from the dev profile
             :1.5 {:dependencies [[org.clojure/clojure       "1.5.0-master-SNAPSHOT"]
                                  [ring-mock                 "0.1.1" :exclusions [org.clojure/clojure
                                                                                  hiccup]]
                                  [midje                     "1.5-alpha3" 
                                   :exclusions [org.clojure/clojure]]
                                  [com.stuartsierra/lazytest "1.2.3" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "2.0.3"]]}}
  :aliases {"run-local" ["with-profile" "dev" "run"]
            "all" ["with-profile" "dev:1.5"]}
  :aot :all
  :warn-on-reflection true
  :repositories [["sonatype-snapshots"
                  "http://oss.sonatype.org/content/repositories/snapshots/"]
                 ["stuart" "http://stuartsierra.com/maven2"]
                 ["stuart-snapshots" "http://stuartsierra.com/m2snapshots"]])
