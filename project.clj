(defproject etymology-english "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :plugins [[lein-cljsbuild "0.3.4"]]
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/main.js"
                                   :optimizations
                                   :whitespace
                                   :pretty-print true}}]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/tools.cli "0.2.1"]
                 [clj-time "0.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [prismatic/dommy "0.1.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [cljs-http "0.1.2"]]
  :web-content "public"
  :main etymology-english.core)
