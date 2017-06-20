(ns figgie.core
  (:require [reagent.core :as reagent :refer [atom]]
            [figgie.users :as users]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [figgie.api :as api]
            [figgie.events]
            [figgie.subs]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def default-start-id 42342)

(defn get-next-page
  "Retrieves the list of users"
  []
  (let [users @(subscribe [:users])
        start-id (if (empty? users)
                   default-start-id
                   (:id (last users)))]
    (go (let [new-users (:body (<! (api/get-users start-id)))]
          (dispatch [:load-users new-users])))))

;; -- Domino 5 - View Functions ----------------------------------------------

(defn users
  []
  [:div
   (for [u @(subscribe [:users])]
     (users/user-photo u))]
  )

(defn photo-wall
  "Renders all profile photos that have been retrieved"
  []
  [:div
   [:h2 "Users"]
   [:div "Count: " @(subscribe [:users-count])
    [:div
     [:button {:on-click #(dispatch [:request-it])
               :disabled @(subscribe [:loading?])}
      "Load Users"]
     [users]]]])

(defn ui
  []
  [:div
   [photo-wall]])

(defn mount-root []
  (reagent/render [ui] (.getElementById js/document "app")))

(defn init! []
  (dispatch-sync [:initialize])     ;; puts a value into application state
  (get-next-page)
  (mount-root))

