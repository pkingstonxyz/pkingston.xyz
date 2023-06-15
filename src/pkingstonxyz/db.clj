(ns pkingstonxyz.db
  (:require [datalevin.core :as dlv]))

(def schema {:slug    {:db/unique :db.unique/identity
                       :db/valueType :db.type/string}
             :title   {:db/valueType :db.type/string}
             :date    {:db/valueType :db.type/instant}
             :content {:db/valueType :db.type/string}
             :tags    {:db/cardinality :db.cardinality/many
                       :db/valueType :db.type/string}
             :type    {:db/valueType :db.type/string}
             :public  {:db/valueType :db.type/boolean}})

(def blogposts (dlv/get-conn "data/" schema))

(comment
  (dlv/transact!
   blogposts
   [{:slug "fifth-post"
     :title "Fifth Post"
     :date (new java.util.Date)
     :content 
"# Some MORE markdown

Do paragraphs work?

Please??

## Here's a subheading

poggies.
poggies."
     :tags ["christianity" "programming" "travel"]
     :type "blog"
     :public true}])
  (dlv/q '[:find [?tags ...] :where [?e :tags ?tags] [?e :slug "fifth-post"]] (dlv/db blogposts))

(dlv/transact! 
 blogposts
 [{:slug "first-post"
   :title "First Post"
   :date (new java.util.Date)
   :content "#Some markdown

            content. Here is a sentence."
   :tags ["foo" "bar"]
   :type "blog"
   :public true}
  {:slug "second-post"
   :title "Second Post"
   :date (new java.util.Date)
   :content "#Some markdown

            content. Here is a sentence."
   :tags ["foo" "baz"]
   :type "blog"
   :public true}
  {:slug "third-post"
   :title "Third Post"
   :date (new java.util.Date)
   :content "#Some markdown

            content. Here is a sentence."
   :tags ["bar" "baz"]
   :type "blog"
   :public true}])

(dlv/q '[:find ?slug
         :in $ ?tag1 ?tag2 ?tag3
         :where 
         [?e :slug ?slug]
         [?e :tags ?tag1]
         [?e :tags ?tag2]]
       (dlv/db blogposts) "foo" "bar" nil))

(defn get-blog-tags []
  (let [q (dlv/q '[:find [?tags ...]
                   :where 
                   [?e :tags ?tags] 
                   [?e :type "blog"]
                   [?e :public true]] (dlv/db blogposts))]
    (set q)))

(defn get-all-blog-headings
  []
  (dlv/q '[:find ?title ?slug ?date
           :keys title slug date
           :where [?e :title ?title]
                  [?e :slug ?slug]
                  [?e :date ?date]
                  [?e :type "blog"]
                  [?e :public true]] (dlv/db blogposts)))
(defn get-blog-headings-by-tag
  ([tag1] (dlv/q '[:find ?title ?slug ?date
                   :keys title slug date
                   :in $ ?tag1
                   :where 
                   [?e :type "blog"]
                   [?e :public true]
                   [?e :title ?title]
                   [?e :slug ?slug]
                   [?e :date ?date]
                   [?e :tags ?tag1]] (dlv/db blogposts) tag1))

  ([tag1 tag2] (dlv/q '[:find ?title ?slug ?date
                        :keys title slug date
                        :in $ ?tag1 ?tag2
                        :where 
                        [?e :type "blog"]
                        [?e :public true]
                        [?e :title ?title]
                        [?e :slug ?slug]
                        [?e :date ?date]
                        [?e :tags ?tag1]
                        [?e :tags ?tag2]] (dlv/db blogposts) tag1 tag2))
  ([tag1 tag2 tag3] (dlv/q '[:find ?title ?slug ?date
                             :keys title slug date
                             :in $ ?tag1 ?tag2 ?tag3
                             :where 
                             [?e :type "blog"]
                             [?e :public true]
                             [?e :title ?title]
                             [?e :slug ?slug]
                             [?e :date ?date]
                             [?e :tags ?tag1]
                             [?e :tags ?tag2]
                             [?e :tags ?tag3]] (dlv/db blogposts) tag1 tag2 tag3)))

(defn get-blog-post-by-slug [slug]
  (first
  (dlv/q '[:find ?title ?date ?content
           :keys title date content
           :in $ ?slug
           :where 
           [?e :slug ?slug]
           [?e :title ?title]
           [?e :date ?date]
           [?e :content ?content]
           [?e :public true]
           [?e :type "blog"]
           ] (dlv/db blogposts) slug)))

(defn yyyy-mm-dd [date]
  (let [s (java.text.SimpleDateFormat. "yyyy-MM-dd")]
    (.parse s date)))
(def schema {:slug    {:db/unique :db.unique/identity
                       :db/valueType :db.type/string}
             :title   {:db/valueType :db.type/string}
             :date    {:db/valueType :db.type/instant}
             :content {:db/valueType :db.type/string}
             :tags    {:db/cardinality :db.cardinality/many
                       :db/valueType :db.type/string}
             :type    {:db/valueType :db.type/string}
             :public  {:db/valueType :db.type/boolean}})
(comment
  (require '[next.jdbc :as jdbc])
  (def db {:dbtype "sqlite" :dbname "db.sqlite3"})
  (def ds (jdbc/get-datasource db))
  (def posts (jdbc/execute! ds ["SELECT * FROM blog_post"]))
  (dlv/transact! 
   blogposts 
   (vec
    (for [post posts]
      {:slug (:blog_post/slug post)
       :title (:blog_post/title post)
       :date (yyyy-mm-dd (:blog_post/created_on post))
       :content (:blog_post/content post)
       :type "blog"
       :tags (let [title (:blog_post/title post)]
               (cond 
                 (= title "My Project Queue") ["project" "code" "music"]
                 (= title "The prod commit") ["code"]
                 (= title "100x100") ["manga"]
                 (= title "Topic List") ["meta"]
                 (= title "Barbershop") ["music"]
                 (= title "The making of kunismos") ["project" "code"]
                 (= title "A personal philokalia") ["christianity"]
                 (= title "Learning Plans") ["project" "learning"]
                 ))
       :public true}))))
