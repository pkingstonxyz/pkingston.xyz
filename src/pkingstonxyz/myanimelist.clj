(ns pkingstonxyz.myanimelist
  (:require [org.httpkit.client :as hkclient]
            [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:import [java.time Instant Duration]))

(defonce cache
  (atom {:last-called nil
         :work nil}))

(defn now [] (Instant/now))

(defn cache-expired? [last-copied]
  (or (nil? last-copied)
      (> (.toMinutes (Duration/between last-copied (now))) 15)))

(defn zip-str [s]
  (zip/xml-zip 
      (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(defn get-recent-manga []
  (when (cache-expired? (:last-called @cache))
    (let [{:keys [body]} @(hkclient/get "https://myanimelist.net/rss.php?type=rm&u=pkingstonxyz")
          data (zip-str body)
          manga (-> data
                    first
                    :content
                    first
                    :content
                    (nth 4))
          title (-> manga
                    :content
                    first
                    :content
                    first)
          link (-> manga
                   :content
                   second
                   :content
                   first)
          work {:title title :link link}]
      (reset! cache (assoc @cache :work work))))
  (:work @cache)) 
