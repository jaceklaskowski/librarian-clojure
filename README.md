# librarian-clojure [![Build Status](https://secure.travis-ci.org/jaceklaskowski/librarian-clojure.png)](http://travis-ci.org/jaceklaskowski/librarian-clojure)

Book manager for [Warszawa Java User Group](http://warszawa.jug.pl).

The views (and perhaps other parts, too) are heavily inspired by the [mametipsum](https://github.com/tvaughan/mametipsum) project.

## Running the project

 1. Install [leiningen](https://github.com/technomancy/leiningen)
 
 2. Install [MongoDB](http://www.mongodb.org/)
 
 3. Run MongoDB, e.g. `mongod --dbpath ~/oss/librarian-clojure/data/db`
 
 4. Go into project directory and execute `lein2 run-local`

A browser window shows up with the welcome page of the application.

## License

Copyright (C) 2012 Konrad Garus & Jacek Laskowski

Distributed under the Eclipse Public License, the same as Clojure.
