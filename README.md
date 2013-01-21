# librarian-clojure [![Build Status](https://secure.travis-ci.org/jaceklaskowski/librarian-clojure.png)](http://travis-ci.org/jaceklaskowski/librarian-clojure)

Book manager for [Warszawa Java User Group](http://warszawa.jug.pl) written in [Clojure](http://clojure.org).

The views (and perhaps other parts, too) are heavily inspired by the [mametipsum](https://github.com/tvaughan/mametipsum) and the [Basic marketing site from Bootstrap's Examples](http://twitter.github.com/bootstrap/examples/hero.html) projects.

The project is developed with [Leiningen 2.0.0](https://github.com/technomancy/leiningen).

## Running the project

 1. Install [leiningen](https://github.com/technomancy/leiningen)
 2. Install [MongoDB](http://www.mongodb.org/)
 3. Run MongoDB, e.g. `mongod --dbpath ~/oss/librarian-clojure/data/db`
 4. Go into project's directory and execute `lein run-local`

A browser window shows up with the welcome page of the application.

## Running the project in REPL (with `lein repl`)

 1. Go into project's directory
 2. Execute `lein repl`
 3. In REPL, execute `(go)` to start a server and open the welcome page.
 4. To stop the server, execute `(stop)`.

## How to contribute (test first, please)

 1. Open a terminal and fire up `lein midje --lazytest`
 2. Write a test that fails (see [Issues](https://github.com/jaceklaskowski/librarian-clojure/issues) for ideas)
 3. Fix, commit and send a pull request
 4. Rinse and repeat

## License

Copyright (C) 2012 [Konrad Garus](https://github.com/konrad-garus), [Adrian Gruntkowski](https://github.com/zoldar), [Jacek Laskowski](https://github.com/jaceklaskowski), and
[contributors](https://github.com/jaceklaskowski/librarian-clojure/graphs/contributors)

Distributed under the Eclipse Public License, the same as Clojure.
