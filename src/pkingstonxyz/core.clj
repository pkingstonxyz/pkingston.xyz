(ns pkingstonxyz.core
  (:require [org.httpkit.server :as hk]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.coercion.malli]
            [reitit.ring.malli]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.multipart :as multipart]
            [ring.middleware.session :as session]
            [malli.util :as mu]
            [pkingstonxyz.pages.homep :as homep]
            [pkingstonxyz.pages.blogp :as blogp]
            [pkingstonxyz.pages.adminp :as adminp]
            [pkingstonxyz.pages.momblogp :as momblogp]
            [pkingstonxyz.auth :as auth])
  (:gen-class))

(def app
  (ring/ring-handler
    (ring/router
      [
       ;["/api"
       ;["/math" {:get {:parameters {:query {:x int?, :y int?}}
       ;                :responses  {200 {:body {:total int?}}}
       ;                :handler    (fn [{{{:keys [x y]} :query} :parameters}]
       ;                              {:status 200
       ;                               :body   {:total (+ x y)}})}}]]
       ["/" {:get {:handler (fn [_] 
                              {:status 200 
                               :content-type "text/html" 
                               :body homep/homep})}}]
       ["/blog" 
        [""
         {:get {:handler (fn [{{tags "tag"} :query-params}]
                           {:status 200
                            :body (blogp/blogp tags)})}
          :post {:handler (fn [{{tags "tag"} :form-params}]
                            {:status 200
                             :body (blogp/filteredpostlist tags)})}}]
        ["/:slug" {:get {:parameters {:path {:slug string?}}
                         :handler (fn [{{slug :slug} :path-params}] 
                                    {:status 200 
                                     :content-type "text/html" 
                                     :body (blogp/blogpost slug)})}}]]
       ["/momblog"
        [""
         {:get {:handler momblogp/momblogp}}]]
       ["/admin"
        ["" {:get {:handler (fn [_]
                              {:status 200
                               :content-type "text/html"
                               :body adminp/loginp};login form
                              )}
             :post {:handler auth/login}}]
        ["/pages" {:middleware [auth/authen-middleware auth/author-middleware]
                   :get {:handler (fn [req]
                                    (clojure.pprint/pprint req)
                                    {:status 200
                                     :content-type "text/html"
                                     :body adminp/adminp})}}]
        ["/blog" 
         ["" {:middleware [auth/authen-middleware auth/author-middleware]
              :get {:handler (fn [_]
                               {:status 200
                                :content-type "text/html"
                                :body (adminp/blogp)})}}]
         ["/:slug" {:middleware [auth/authen-middleware auth/author-middleware]
                    :get {:handler (fn [{{slug :slug} :path-params}]
                                     {:status 200
                                      :content-type "text/html"
                                      :body (adminp/managecomp slug)})}
                    :put {:handler adminp/edit-post!}
                    :delete {:parameters {:form-params {:delete string?}}
                             :handler adminp/delete-post!}

                    :post {:handler adminp/make-post!}
                    }]]
        ["/momblog"
         ["" {:middleware [auth/authen-middleware auth/author-middleware]
              :get {:handler momblogp/adminp}
              :post {:parameters {:multipart [:map [:file reitit.ring.malli/temp-file-part]]}
                     :handler momblogp/post!}
              :delete {:handler momblogp/delete!}}]]]]
      ;; router data affecting all routes
      {:data {:coercion (reitit.coercion.malli/create
                          {:error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                           :compile mu/closed-schema
                           :strip-extra-keys true
                           :default-values true
                           :options nil})
                           
              :muuntaja   m/instance
              :middleware [parameters/parameters-middleware
                           muuntaja/format-middleware
                           rrc/coerce-exceptions-middleware
                           rrc/coerce-response-middleware
                           rrc/coerce-request-middleware
                           multipart/multipart-middleware]}})
    (ring/routes
      (ring/create-resource-handler {:path "/"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404, :body "404, not found"})
         :method-not-allowed (constantly {:status 405, :body "405, not allowed"})
         :not-acceptable (constantly {:status 406, :body "406, not acceptable"})}))
    {:middleware [session/wrap-session]}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Runnit")
  (hk/run-server app {:port 8080}))
