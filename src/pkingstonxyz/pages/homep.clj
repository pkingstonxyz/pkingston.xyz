(ns pkingstonxyz.pages.homep
  (:require [hiccup.page :as h]))

(def homep
  (h/html5
    [:head
     [:title "Home"]
     [:link {:rel "stylesheet" :href "/css/home.css"}]
     [:link {:rel "stylesheet" :href "/css/base.css"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:charset "utf-8"}]]
    [:body
     [:main
      [:section.snap
       [:span
        [:p.hiim "Hi! I'm"]
        [:p.patrick "Patrick"]]]
      [:section.snap
       [:span
        [:p.large "I am a:"]
        [:a {:href "/blog?tag=code"}
         [:div#swe.reveal
          [:p.normal "&lt;Coder/&gt;"]
          [:span.code.small "⌨️ "]
          [:span.code.small "λ"]
          [:span.code.small "<>"]
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
          [:span.emoji.small "🚃"]]]]]
      [:section.snap
       [:span
        [:p.large "See my"]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "/PatrickKingstonResume.pdf"} [:p.normal "Resume"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "/blog"} [:p.normal "Blog"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "/blog?tag=project"} [:p.normal "Projects"]]]
        [:div.backgroundreveal
         [:div.background]
         [:a {:href "mailto:patrick@pkingston.xyz"} [:p.normal "Contact"]]]
        ;[:p.normal "youtube"] ;Coming soon...
        ]]
      [:section.snap]]
     [:script {:src "/js/home.js"}]]))
