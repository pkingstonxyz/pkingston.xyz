(ns pkingstonxyz.rssfeed
  (:require [hiccup2.core :as hc]
            [pkingstonxyz.db :as db]
            [clojure.string]
            )
  (:import (java.text SimpleDateFormat)
           (java.util Locale)))

(defn rssfeed [_]
  {:status 200
   :type "text/rss+xml"
   :body 
   (let [posts (reverse (sort-by :date (db/get-all-blog-headings)))
         formatter (SimpleDateFormat. "EEE, dd MMM yyyy HH:mm:ss Z" Locale/US)]
     (str
     (hc/html [:rss {:version "2.0"
                     :xmlns:atom "http://www.w3.org/2005/Atom"}
               [:channel
                [:title "Patrick Kingston's Blog"]
                [:link "https://pkingston.xyz/blog"]
                [:description "Where I, Patrick, blog about things that I like, think about, or want to talk about. Mostly programming, language learning, and some music."]
                [:language "en-us"]
                (hc/raw "<atom:link href=\"https://pkingston.xyz/rss\" rel=\"self\" type=\"application/rss+xml\"/>")
                (for [post posts]
                  [:item
                   [:title (:title post)]
                   [:pubDate (.format formatter (:date post))]
                   [:link (str "https://pkingston.xyz/blog/" (:slug post))]
                   [:guid (str "https://pkingston.xyz/blog/" (:slug post))]])]])))})

