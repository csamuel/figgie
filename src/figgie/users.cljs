(ns figgie.users
  (:require [reagent.core :as reagent :refer [atom]]
            [figgie.api :as api]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;(defonce app-state (reagent/atom {:users   []
;                                  :loading false}))
(def default-start-id 4312)

;(defn new-board [n]
;  (vec (repeat n (vec (repeat n "B")))))

(defn get-next-page
  "Retrieves the list of users"
  [users]
  (let [start-id
        (if (not (empty? @users))
          (:id (last @users))
          default-start-id)]
    ;(swap! app-state assoc-in [:loading] true)
    (go (let [theusers (:body (<! (api/get-users start-id)))]
          (reset! users (into @users theusers))))))

(defn user-photo [user]
  "Renders a users profile photo"
  (let [{:keys [id html_url avatar_url]} user]
    ^{:key id}
    [:a {:href   html_url
         :target "_blank"}
     [:img {:src    avatar_url
            :height 32
            :width  32}]]))

(defn users-page
  "Renders all profile photos that have been retrieved"
  [users]
  [:div
   [:h2 "Users"]
   [:div "Current count: " (count @users)
    [:div
     [:button {:on-click (fn [e] (get-next-page users))}
      "Load Users"]
     [:div
      (for [u @users]
        (user-photo u))]]]])
