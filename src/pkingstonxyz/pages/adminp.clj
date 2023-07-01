(ns pkingstonxyz.pages.adminp
  (:require [hiccup.page :as h]
            [hiccup.core :as hc]
            [hiccup.util :as hu]
            [pkingstonxyz.db :as db]
            [markdown.core :as md]))

(def loginp 
  (h/html5
   [:head [:meta {:charset "utf-8"}]]
   [:body
    [:form {:method "post"}
     [:input {:type "text" :name "username"}]
     [:input {:type "password" :name "password"}]
     [:input {:type "submit"}]]]))

(def adminp
  (h/html5
   [:head [:meta {:charset "utf-8"}]]
   [:body
    [:a {:href "/admin/blog"} [:p "Edit Blog Posts"]]
    [:a {:href "/admin/momblog"} [:p "Edit momblog posts"]]]))

(defn blogp
  []
  (h/html5
   [:head [:meta {:charset "utf-8"}]
     [:script {:src "https://unpkg.com/htmx.org@1.9.2"}]]
   [:body
    [:div
     [:h2 "new post"]
     [:form {:hx-post "/admin/blog/newpostdontusethisslug"
             :hx-swap "outerHTML"
             :hx-trigger "submit"}
      [:label "Title"]
      [:input {:type "text" :name "title"}]
      [:label "Public"]
      [:input {:type "checkbox" :name "public"}]
      [:label "Content"]
      [:textarea {:name "content"}]
      [:input {:type "submit"}]]]
    (for [post (db/get-all-blog-headings-admin)]
      [:div {:hx-get (str "/admin/blog/" (:slug post))
             :hx-swap "outerHTML"
             :hx-trigger "click"}
       [:p (:title post)]])]))

(defn managecomp
  [slug]
  (hc/html
  (let [post (db/get-blog-post-by-slug-admin slug)]
    [:div {:id (str "id" slug)}
     [:h1 "Edit"]
     [:form {:hx-put (str "/admin/blog/" slug)
             :hx-target (str "#id" slug)
             :hx-trigger "submit"} 
      [:label "Title"]
      [:input {:type "text" :name "title" :value (:title post)}]
      [:label "Public"]
      [:input {:type "checkbox" :name "public" :checked (str (:public post))}]
      [:label "Content"]
      [:textarea {:name "content"} (:content post)]
      [:input {:type "submit"}]]
     [:h1 "Delete"]
     [:form {:hx-delete (str "/admin/blog/" slug)
             :hx-confirm "Are you *positive*"
             :hx-target (str "#id" slug)
             :hx-trigger "submit"}
      [:input {:type "checkbox" :name "delete"}]
      [:input {:type "submit"}]]])))

(defn edit-post!
  [{{title "title" content "content" public "public"} :params
    {slug :slug} :path-params}]
  (let [edit-result (db/edit-blog-post! slug title content public)]
    (if edit-result
      {:status 200
       :content-type "text/html"
       :body "POST EDITED"}
      {:status 404
       :content-type "text/html"
       :body "FAILED TO EDIT"})))

(defn delete-post!
  [{{delete "delete"} :form-params
    {slug :slug} :path-params}]
  (if (= "on" delete)
    (let [delete-result (db/delete-blog-post! slug)]
      (if delete-result
        {:status 200
         :content-type "text/html"
         :body "POST DELETED"}
        {:status 404
         :content-type "text/html"
         :body "FAILED TO DELETE"}))
    {:status 200
     :content-type "text/html"
     :body "POST NOT DELETED"}))

(defn make-post!
  [{{title "title" content "content" public "public"} :params}]
  (println "MAKING")
  (println title)
  (println content)
  (println public)
  (let [make-result (db/make-blog-post! title content public)]
    (if make-result
      {:status 200
       :content-type "text/html"
       :body "POST CREATED"}
      {:status 404
       :content-type "text/html"
       :body "FAILED TO CREATE"})))
