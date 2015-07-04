(def crash-version "1.3.1")

(defproject net.dossot/crashure "1.0.0-SNAPSHOT"
  :description "Clojure support for [CRaSH](http://www.crashub.org/)."
  :url "https://www.github.com/ddossot/crashure"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"
            :comments "Copyright (c) 2015 David Dossot"}

  :profiles {:dev {:plugins [[lein-kibit "0.1.2"]
                             [jonase/eastwood "0.2.1"]]
                   :dependencies [[org.crashub/crash.connectors.telnet ~crash-version]]}}

  :dependencies
  [
   [org.clojure/clojure "1.6.0"]

   [org.crashub/crash.shell ~crash-version]
   [prismatic/schema "0.4.3"]
  ]

  :release-tasks [["vcs" "assert-committed"]
                  ["clean"]
                  ["kibit"]
                  ["eastwood"]
                  ["test"]
                  ["change" "version"
                   "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy" "clojars"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]

  :deploy-repositories [["releases" :clojars]]
)
