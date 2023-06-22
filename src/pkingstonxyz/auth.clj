(ns pkingstonxyz.auth
  (:require [buddy.auth :as ba]
            [buddy.auth.backends :as ba-backends]
            [buddy.auth.backends.httpbasic :as ba-httpbasic]
            [buddy.auth.middleware :as ba-middleware]
            [buddy.hashers :as buddy-hashers]
            [buddy.sign.jwt :as jwt]
            [datalevin.core :as dlv]
            [ring.util.response :refer [redirect]]))

(def schema {:username {:db/valueType :db.type/string
                        :db/unique :db.unique/identity}
             :password {:db/valueType :db.type/string}})

(def db (dlv/get-conn "data/auth" schema))

(defn get-user
  [username]
  (first (dlv/q '[:find ?username ?password
                  :keys username password
                  :in $ ?username
                  :where [?e :username ?username]
                         [?e :password ?password]] (dlv/db db) username)))

(defn authenticate
  [username password]
  (let [user (get-user username)]
    (if (and user
             (buddy-hashers/check password (:password user)))
      (dissoc user :password)
      nil)))

(defn authen-middleware 
  [handler]
  (ba-middleware/wrap-authentication handler (ba-backends/session)))

(defn author-middleware
  [handler]
  (fn [req]
    (if (ba/authenticated? req)
      (handler req)
      (redirect "/admin"))))

(defn login [req]
  (let [username (get-in req [:form-params "username"])
        password (get-in req [:form-params "password"])
        user (authenticate username password)]
    (println "LOGIN " username " " password " | " user)
    (if (nil? user)
      (redirect "/admin")
      (let [next-session (assoc (:session req) :identity user)]
      (-> (redirect "/admin/pages")
          (assoc :session next-session))))))

(comment
  (dlv/transact! db [{:username "foo"
                      :password (buddy-hashers/encrypt "bar")}
                     {:username "baz"
                      :password (buddy-hashers/encrypt "qux")}])
  (get-user db "foo")
  (get-user db "baz")
  (get-user db "quz"))
