(ns pkingstonxyz.ankistats
  (:require [next.jdbc :as jdbc]
            [clojure.string])
  (:import [java.time Instant]
           [java.time.temporal ChronoUnit]))

;; Update this path to match your real Anki collection path
(def db-spec {:dbtype "sqlite"
              :dbname "/Users/pkingston/Library/Application Support/Anki2/User 1/collection.anki2"})

(defn current-time-millis []
  (-> (Instant/now)
      (.toEpochMilli)))

(defn time-24h-ago []
  (-> (Instant/now)
      (.minus 24 ChronoUnit/HOURS)
      (.toEpochMilli)))

(defn reviews-last-24h []
  (let [ds (jdbc/get-datasource db-spec)
        since (time-24h-ago)]
    (jdbc/execute! ds
                   ["SELECT r.*, c.did, d.name AS deck_name
                    FROM revlog r
                    JOIN cards c ON r.cid = c.id
                    JOIN decks d ON c.did = d.id
                    WHERE r.id >= ?" since])))

(defn get-study-data []
  (let [reviews (reviews-last-24h)
        decklist (map :decks/deck_name reviews)
        normalized (map #(first (clojure.string/split %1 #"\u001F")) decklist)
        data (frequencies normalized)]
    data)) 
