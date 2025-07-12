(ns pkingstonxyz.ankistats
  (:require [next.jdbc :as jdbc]
            [clojure.string]
            [clojure.java.io :as io])
  (:import [java.time Instant Duration]
           [java.time.temporal ChronoUnit]))

;; Update this path to match your real Anki collection path
(def copypath "/tmp/ankidb.anki2")
(def db-path "/Users/pkingston/Library/Application Support/Anki2/User 1/collection.anki2")
(def db-spec {:dbtype "sqlite"
              :dbname (str "file:" copypath "?mode=ro")
              :connection-uri? true})

(defonce cache-state
  (atom {:last-copied nil})) ; stores {:last-copied <Instant>}

(defn now [] (Instant/now))

(defn cache-expired? [last-copied]
  (or (nil? last-copied)
      (> (.toMinutes (Duration/between last-copied (now))) 15)))

(defn ensure-cached []
  (let [{:keys [last-copied]} @cache-state]
    (when (cache-expired? last-copied)
      (println "Cache expired — copying Anki DB...")
      (io/copy (io/file db-path) (io/file copypath))
      (swap! cache-state assoc :last-copied (now)))))

(defn time-24h-ago []
  (-> (Instant/now)
      (.minus 24 ChronoUnit/HOURS)
      (.toEpochMilli)))

(defn reviews-last-24h []
  (let [_ (ensure-cached)
        ds (jdbc/get-datasource db-spec)
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
