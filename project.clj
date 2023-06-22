(defproject pkingstonxyz "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [metosin/reitit "0.7.0-alpha4"]
                 [hiccup "1.0.5"]
                 [http-kit "2.7.0-RC1"]
                 [datalevin "0.8.16"]
                 [markdown-clj "1.11.4"]
                 [com.github.seancorfield/next.jdbc "1.3.874"]
                 [org.xerial/sqlite-jdbc  "3.39.2.1"]
                 [buddy "2.0.0"]]
  :main ^:skip-aot pkingstonxyz.core
  :target-path "target/%s"
  :jvm-opts ["--add-opens=java.base/java.nio=ALL-UNNAMED"
             "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
