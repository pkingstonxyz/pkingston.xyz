(ns pkingstonxyz.ankistats
  (:require [next.jdbc :as jdbc]
            [clojure.string]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh])
  (:import [java.time Instant Duration]
           [java.time.temporal ChronoUnit]))

;; Update this path to match your real Anki collection path
(def copypath "/tmp/ankidb.anki2")
(def db-path "/Users/pkingston/Library/Application Support/Anki2/User 1/collection.anki2")
(def db-spec {:dbtype "sqlite"
              :dbname (str "file:" copypath "?mode=ro&nolock")
              :connection-uri? true})

(defonce cache-state
  (atom {:last-copied nil})) ; stores {:last-copied <Instant>}

(defn now [] (Instant/now))

(defn cache-expired? [last-copied]
  (or (nil? last-copied)
      (> (.toMinutes (Duration/between last-copied (now))) 60)))

#_(defn ensure-cached []
  (let [{:keys [last-copied]} @cache-state
        shmpath (str copypath "-shm")
        walpath (str db-path "-wal")
        walcopypath (str copypath "-wal")]
    (when (cache-expired? last-copied)
      (println "Cache expired — copying Anki DB...")
      (try
        (when (io/file shmpath)
          (io/delete-file (io/file shmpath)))
        (catch Exception e (str "Bruh the file didn't delete: " e)))
      (try
        (when (io/file copypath)
          (io/delete-file (io/file copypath)))
        (catch Exception e (str "Bruh the file didn't delete: " e)))
      (try
        (when (io/file walcopypath)
          (io/delete-file (io/file walcopypath)))
        (catch Exception e (str "Bruh the file didn't delete: " e)))
      (try
        (io/copy (io/file db-path) (io/file copypath))
        (catch Exception e (str "Bruh the file didn't copy: " e)))
      (try
        (io/copy (io/file walpath) (io/file walcopypath))
        (catch Exception e (str "Bruh the file didn't copy: " e)))
      (swap! cache-state assoc :last-copied (now)))))

(defn ensure-cached []
  (when (cache-expired? (:last-copied @cache-state))
    (try
      (let [output (sh/sh "sudo" "systemctl" "stop" "ankisync.service")]
        (println output))
      (catch Exception e (str "Failed to stop ankisync due to: " e)))
    (try
      (let [output (sh/sh "sqlite3" db-path (str "\".backup\" '" copypath "'"))]
        (println output))
      (catch Exception e (str "Failed to backup server due to:" e)))
    (try
      (let [output (sh/sh "sudo" "systemctl" "start" "ankisync.service")]
        (println output))
      (catch Exception e (str "Failed to start ankisync due to: " e)))
    (swap! cache-state assoc :last-copied (now))))

(defn time-24h-ago []
  (-> (Instant/now)
      (.minus 72 ChronoUnit/HOURS)
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

(get-study-data)
