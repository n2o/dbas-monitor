(defproject monitor "0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/core.async "0.3.442"]
                 [clj-http "3.5.0"]
                 [com.draines/postal "2.0.2"]]
  :main ^:skip-aot monitor.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
