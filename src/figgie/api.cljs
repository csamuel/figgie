(ns figgie.api
  (:require [cljs-http.client :as http]))

(def auth-params {:client_id     "bafbe98cb9adc18d7455"
                  :client_secret "def626063dde348f7d859d1e1d0cd9e795a763cf"})

(def default-page-size 100)

(defn get-users
  "Make API request to return users"
  [since-id]
  (http/get "https://api.github.com/users"
            {:with-credentials? false
             :query-params      (merge auth-params {:since    since-id
                                                    :per_page default-page-size})}))