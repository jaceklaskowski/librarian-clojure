# librarian-clojure [![Build Status](https://secure.travis-ci.org/jaceklaskowski/librarian-clojure.png)](http://travis-ci.org/jaceklaskowski/librarian-clojure)

Book manager for [Warszawa Java User Group](http://warszawa.jug.pl) written in [Clojure](http://clojure.org).

The views (and perhaps other parts, too) are heavily inspired by the [mametipsum](https://github.com/tvaughan/mametipsum) and the [Basic marketing site from Bootstrap's Examples] (http://twitter.github.com/bootstrap/examples/hero.html) projects.

## Running the project

 1. Install [leiningen](https://github.com/technomancy/leiningen)
 2. Install [MongoDB](http://www.mongodb.org/)
 3. Run MongoDB, e.g. `mongod --dbpath ~/oss/librarian-clojure/data/db`
 4. Go into project directory and execute `lein2 run-local`

A browser window shows up with the welcome page of the application.

## How to contribute (test first please)

 1. Open a terminal and fire up `lein2 midje --lazytest`
 2. Write a test that fails
 3. Fix it and send a pull request
 4. Rinse and repeat

## License

Copyright (C) 2012 [Konrad Garus](https://github.com/konrad-garus), [Adrian Gruntkowski](https://github.com/zoldar), [Jacek Laskowski](https://github.com/jaceklaskowski)

Distributed under the Eclipse Public License, the same as Clojure.
