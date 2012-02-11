;; http://corfield.org/blog/post.cfm/testing-your-project-against-multiple-versions-of-clojure
(def common-deps '[[ring/ring-jetty-adapter "1.0.1"]
                   [ring/ring-devel "1.0.1"]
                   [compojure "0.6.4"]
                   [hiccup "0.3.8"]
				           [org.mongodb/mongo-java-driver "2.6.5"]
                   [congomongo "0.1.7"]])

(defproject librarian-clojure "0.0.1-SNAPSHOT"
  :description "Book manager in Clojure"
  :url "http://github.com/jaceklaskowski/librarian-clojure"
  :repositories [["sonatype-snapshots"
                  "https://oss.sonatype.org/content/repositories/snapshots/"]]
  :dependencies ~(conj common-deps
                       '[org.clojure/clojure "1.3.0"])
  :repl-init librarian-clojure.repl
  :multi-deps {"1.4.0" ~(conj common-deps
                       '[org.clojure/clojure "1.4.0-master-SNAPSHOT"])}
  :dev-dependencies [[lein-multi "1.1.0-SNAPSHOT"]
                     [lein-ring "0.5.4"]
                     [lein-eclipse "1.0.0"]]
  :ring {:handler librarian-clojure.core/app})
