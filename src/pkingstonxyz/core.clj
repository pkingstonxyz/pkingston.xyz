(ns pkingstonxyz.core
  (:require [org.httpkit.server :as hk]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [pkingstonxyz.pages.homep :as homep]
            [pkingstonxyz.pages.blogp :as blogp])
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
                                      :body (blogp/blogpost slug)})}}]]]
      ;; router data affecting all routes
      {:data {:coercion   reitit.coercion.spec/coercion
              :muuntaja   m/instance
              :middleware [parameters/parameters-middleware
                           rrc/coerce-request-middleware
                           muuntaja/format-response-middleware
                           rrc/coerce-response-middleware]}})
    (ring/routes
      (ring/create-resource-handler {:path "/"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404, :body "404, not found"})
         :method-not-allowed (constantly {:status 405, :body "405, not allowed"})
         :not-acceptable (constantly {:status 406, :body "406, not acceptable"})}))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Runnit")
  (hk/run-server app {:port 8080}))
