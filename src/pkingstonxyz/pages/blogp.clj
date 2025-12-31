(ns pkingstonxyz.pages.blogp
  (:require [hiccup.page :as h]
            [hiccup2.core :as hc]
            [pkingstonxyz.db :as db]
            [pkingstonxyz.pages.topscroll :as topscroll]
            [pkingstonxyz.pages.favicon :as favicon]
            [markdown.core :as md]))


(defn tagchips [querytags]
  (let [alltags (db/get-blog-tags)
        qtset (set querytags)]
    [:section#tagchips
     [:h3 "Filter by tag " [:span.emoji "🔍"]]
     [:form
      {:hx-post "/blog"
       :hx-trigger "change"
       :hx-target "#postlist"
       :hx-indicator "#filter-indicator"
       :hx-swap "outerHTML swap:.2s"}
      (for [tag alltags]
        [:span.tagchip
         (if (qtset tag)
           [:input {:type "checkbox" :name "tag" :value tag :id tag :checked true}]
           [:input {:type "checkbox" :name "tag" :value tag :id tag}])
         [:label {:for tag} tag]])]
     [:div#filter-indicator 
      [:p {:style "display: inline;"} "Loading "]
      [:span.spinner.emoji "🍥"]]]))

(defn currentyear []
  (let [now (java.util.Date.)]
    (+ 1900 (.getYear now))))

(defn postcard [post]
  [:a {:href (str "/blog/" (:slug post))}
   [:article.postcard
    [:h3 (str (:title post))]
    [:p (str "Posted: " (.format (java.text.SimpleDateFormat. "dd/MM/yyyy") (:date post)))]
    [:p (str "Tags: " (apply str (interpose ", " (:tags post))))]]])

(defn _filteredpostlist [tags]
  (let [taglist (filter identity (flatten [tags]))]
    [:section#postlist
     (cond 
       (empty? taglist)
       (let [posts (reverse (sort-by :date (db/get-all-blog-headings)))]
         (for [post posts]
           (postcard post)))
       (>= 3 (count taglist))
       (let [posts (reverse (sort-by :date (apply db/get-blog-headings-by-tag taglist)))]
         (if (empty? posts)
           [:p "No posts found!"]
           (for [post posts]
             (postcard post))))
       :else
       [:p "No posts found!"])]))

(defn filteredpostlist [tags]
  (str (hc/html
    (_filteredpostlist tags))))

(defn blogp [tags]
  (str (h/html5
    [:head
     [:title "Blog"]
     favicon/favicon
     [:link {:rel "stylesheet" :href "/css/base.css"}]
     [:link {:rel "stylesheet" :href "/css/blog.css"}]
     [:script {:src "https://unpkg.com/htmx.org@1.9.2"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:charset "utf-8"}]]
    [:body
     (topscroll/topelement)
     [:main
      [:header.header
       [:h1 [:a {:href "/"} "Patrick's "] [:span.gradient "Blog"]]]
      [:hr]
      (tagchips tags)
      [:hr]
      (_filteredpostlist tags)]
     [:footer.footer
      [:p (str "© " (currentyear) " Patrick Kingston")]]
     [:script {:src "/js/home.js"}]
     ])))

(defn blogpage 
  [{:keys [title date content]}]
  (str (h/html5 
    [:head
     [:title title]
     favicon/favicon
     [:link {:rel "stylesheet" :href "/css/base.css"}]
     [:link {:rel "stylesheet" :href "/css/blogpost.css"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:charset "utf-8"}]
     ;Code support
     [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/styles/default.min.css"}]
     [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/highlight.min.js"}]
     [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/languages/clojure.min.js"}]
     [:script "hljs.highlightAll();"]]
    [:body
     (topscroll/topelement)
     [:header [:a {:href "/"}
               "Patrick's "] [:span.gradient "Blog"]]
     [:main
      [:article
       [:h1.title title]
       [:p.date (str "Last Edited: " date)]
       [:hr]
       (md/md-to-html-string content)]]
     [:footer "© " (currentyear) " Patrick Kingston"]
     [:script {:src "/js/home.js"}]])))

(defn blogpost [slug]
  (let [post (db/get-blog-post-by-slug slug)]
    (if (nil? post)
      "Page not found!"
      (blogpage post))))
