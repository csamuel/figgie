(ns figgie.events
  (:require [reagent.core :as reagent :refer [atom]]
            [figgie.users :as users]
            [re-frame.core :refer [reg-event-db subscribe dispatch reg-event-fx]]
            [figgie.api :as api]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def default-start-id 4312)

(reg-event-db
  :initialize
  (fn [_ _]
    {:loading? false
     :users    []}))

(reg-event-db
  :load-users
  (fn [db [_ new-users]]
    (let [current-users @(subscribe [:users])
          start-id (if (empty? current-users)
                     default-start-id
                     (:id (last current-users)))]
      (-> db
          (assoc :loading? false)
          (assoc :users (into current-users new-users))))))

(reg-event-db
  :request-it
  (fn
    [db _]
    (let [users @(subscribe [:users])
          start-id (if (empty? users)
                     default-start-id
                     (:id (last users)))]
      (go (let [new-users (:body (<! (api/get-users start-id)))]
            (dispatch [:load-users new-users])))
      (assoc db :loading? true))))
