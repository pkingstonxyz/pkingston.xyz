(ns pkingstonxyz.pages.homep
  (:require [hiccup.page :as h]
            [hiccup2.core :as h2]
            [pkingstonxyz.pages.topscroll :as topscroll]))

(defn homep []
  (h/html5
    [:head
     [:title "Home"]
     [:link
 {:rel "icon",
  :type "image/png", 
  :href "/favicon-96x96.png", 
  :sizes "96x96"}]
[:link {:rel "icon", :type "image/svg+xml", :href "/favicon.svg"}]
[:link {:rel "shortcut icon", :href "/favicon.ico"}]
[:link
 {:rel "apple-touch-icon",
  :sizes "180x180", 
  :href "/apple-touch-icon.png"}]
[:link {:rel "manifest", :href "/site.webmanifest"}]
     [:link {:rel "stylesheet" :href "/css/home.css"}]
     [:link {:rel "stylesheet" :href "/css/base.css"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:charset "utf-8"}]]
    [:body
     (h2/raw (topscroll/topelement))
     [:main
      [:section.snap
       [:div
        [:p.hiim "Hi! I'm"]
        [:p.patrick [:a.h-card {:href "https://pkingston.xyz"} "Patrick"]]]]
      [:section.snap
       [:div
        [:p.large "I am a:"]
        [:div.container
         [:div
          [:a {:href "/blog?tag=code"}
           [:div#swe.reveal
            [:p.normal "&lt;Coder/&gt;"]
            [:span.code.small "λ"]
            [:span.code.small ""]
            [:span.code.small "github:pkingstonxyz"]
            [:span.code.small "()"]]]
          [:a {:href "/blog?tag=classics"}
           [:div#cla.reveal
            [:p.normal "Classicist"]
            [:span.greek.small "α"]
            [:span.greek.small "📙"]
            [:span.greek.small "ω"]
            [:span.greek.small "🏛️"]]]
          [:a {:href "/blog?tag=christianity"}
           [:div#chr.reveal 
            [:p.normal "Christian"]
            [:span.emoji.small "☦️"]
            [:span.emoji.small "🕯️"]
            [:span.emoji.small "📿"]
            [:span.emoji.small "🔔"]]]
          [:a {:href "/blog?tag=music"}
           [:div#mus.reveal 
            [:p.normal "Musician"]
            [:span.emoji.small "💻"]
            [:span.emoji.small "🎼"]
            [:span.emoji.small "🎧"]
            [:span.emoji.small "📀"]]]
          [:a {:href "/blog?tag=travel"}
           [:div#tra.reveal 
            [:p.normal "Traveler"]
            [:span.emoji.small "🗺️"]
            [:span.emoji.small "&#x1F1EC;&#x1F1F7;"]
            [:span.emoji.small "🧭"]
            [:span.emoji.small "🚃"]]]]
         [:div {:style "margin-left: 10px; width: 10em;"}
          [:p {:style "font-size: 16px;"}
           "Hey welcome to my site. I'm a dev on the Ansible team and you can see my code on github by clicking " 
           [:u [:a {:href "https://github.com/pkingstonxyz"
               :rel "me"}
            "here. "]]
           "I also blog on stuff I'm interested in, so feel free to check out some of my categories over there <-"]]]]]
      [:section.snap
       [:div
        [:p.large "See my"]
        #_[:div.backgroundreveal
         [:div.background]
         [:a {:href "/PatrickKingstonResume.pdf"} [:p.normal "Resume"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "/blog"} [:p.normal "Blog"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "mailto:patrick@pkingston.xyz"
              :rel "me"} [:p.normal "Contact"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "/momblog"} [:p.normal "Image blog"]]]
        ]]
      [:section.snap]]
     [:script {:src "/js/home.js"}]]))
