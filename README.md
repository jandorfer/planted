# Planted.

A tool to track growing things. Lay out your garden or vineyard, saving data
about each plant. You can then come back and continue to enter updates about
each plant as it grows. Over time, you should be able to track relative
performance and hopefully trace it back to some source based on the historical
data you've accumulated. Unless the deer eat your stuff. Then there's no
mystery to solve. Damn deer.

## Running

To start a web server for the application, simply:

    lein run

## Development

The build is two fold: first, use gulp to update the static resources (if
anything has changed.) Then, use leiningen to start up the application or a
repl.

### Gulp

The front-end resources for this project are managed by a gulp build. To get
started, make sure you have node (npm at least) installed then:

    npm install
    gulp

That will rebuild the html/js/css files served. It will also by default watch
the source files, and rebuild as needed when you make changes. The web server
will automatically serve whatever the latest on disk is, so this approach is
fully compatible with any way you want to run the lein project.

### Lein

The back end is managed by lein. There are some user methods available for your
repl'ing convenience:

    (user/go)    ; Starts up the server!
    (user/stop)  ; Shuts it down
    (user/reset) ; Shorthand for "stop, go"

You can also just `lein run` or use `lein uberjar` and run the resulting jar:

    java -jar planted-0.1.0-SNAPSHOT.jar
