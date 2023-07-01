(ns pkingstonxyz.pages.momblogp
  (:require [hiccup.page :as hp]
            [hiccup.core :as h]
            [datalevin.core :as dlv]))

(def schema {:title   {:db/valueType :db.type/string}
             :date    {:db/valueType :db.type/instant}
             :img     {:db/valueType :db.type/string}})

(def conn (dlv/get-conn "data/momblog" schema))

(defn save-file!
  [tempfile title filename]
  (let [filepath (str "resources/public/imgs/blog/" filename)
        imgurl (str "/imgs/blog/" filename)
        date (java.util.Date.)
        _ (clojure.java.io/copy
            tempfile
            (clojure.java.io/file filepath))]
    (dlv/transact! 
      conn
      [{:img imgurl
        :title title
        :date date}])))

(comment
  (let [wah (dlv/q '[:find ?e  ?title ;?img
           :keys e  title
           :in $
           :where [?e :date ?date]
                  [?e :title ?title]
                  ;[?e :img ?img]
           ] (dlv/db conn))]
    (clojure.pprint/pprint wah))
  (let [ foo (dlv/q '[:find (pull ?e [*])
           :in $
           :where [?e :title "ONLY IN NEW YORK"]] (dlv/db conn))]
    (clojure.pprint/pprint foo))
  
  (let [all-posts (dlv/q '[:find ?date ?img ?title
                           :keys date img title
                           :in $
                           :where 
                           [?e :date ?date]
                           [?e :img ?img]
                           [?e :title ?title]] (dlv/db conn))
        posts (->> all-posts
                   (into [])
                   (sort-by :date)
                   (reverse)
                   (partition 4 4 nil)
                   )]
    (clojure.pprint/pprint posts)))

(defn momblogp
  [{{page "page"} :query-params}]
  {:status 200
   :content-type "text/html"
   :body 
  (let [all-posts (dlv/q '[:find ?date ?img ?title
                           :keys date img title
                           :in $
                           :where 
                           [?e :date ?date]
                           [?e :img ?img]
                           [?e :title ?title]] (dlv/db conn))

        posts (->> all-posts
                   (into [])
                   (sort-by :date)
                   (reverse)
                   (partition 4 4 nil)
                   )]
    (println posts)
    (if (nil? page)
      (hp/html5
        [:head
         [:title "Mom blog"]
         [:link {:rel "stylesheet" :href "/css/momblogp.css"}]
         [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
         [:meta {:charset "utf-8"}]
         [:script {:src "https://unpkg.com/htmx.org@1.9.2"}]]
        [:body
         [:header 
          [:svg#greekflag {:width "70" :height "70"}
           [:rect {:x "29" :y "0" :width "12" :height "70"
                   :stroke "none" :fill "white"}]
           [:rect {:x "0" :y "29" :width "70" :height "12"
                   :stroke "none" :fill "white"}]]
          [:h1 "Patrick"]]
         [:main
          (for [post (first posts)]
            [:article
             [:img {:src (:img post)}]
             [:p.title (:title post)]
             [:p.date  (str (:date post))]])]])
      (h/html
        (for [post (nth posts (Integer/parseInt page))]
          [:article
           [:img {:src (:img post)}]
           [:p.title (:title post)]
           [:p.date (str (:date post))]]))))})

(defn adminp
  [_]
  {:status 200
   :content-type "text/html"
   :body 
   (hp/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:script {:src "https://unpkg.com/htmx.org@1.9.2"}]]
     [:body
      [:h1 "Momblogadmin"]
      [:h2 "upload"]
      [:form {:method "post" :encType "multipart/form-data"}
       [:input {:type "file" :name "file"}]
       [:input {:type "text" :name "title"}]
       [:input {:type "submit"}]]])})

(defn post!
  [{{{:keys [filename tempfile size]} "file"
     title "title"} :multipart-params}]
  (save-file! tempfile title filename)
  {:status 200
   :content-type "text/html"
   :body {:title title
          :filename filename
          :size size
          :tempfile tempfile}})

(defn delete!
  [_]
  {:status 200
   :content-type "text/html"
   :body "delete!"})

