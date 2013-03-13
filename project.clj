(defproject game "0.1.0-SNAPSHOT"
  :description "a game about time travel"
  :url "http://game.steveolsen.us"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [compojure "1.1.5"]]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-ring "0.8.3"]]
  :repl-listen-port 9000
  :cljsbuild
  {:builds
   [{:source-paths ["src/game/app"],
     :compiler
     {:pretty-print true,
      :output-to "resources/public/js/game.js",
      :optimizations :whitespace}}]}
  :ring {:handler game.server/app})
