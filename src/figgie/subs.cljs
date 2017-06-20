(ns figgie.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :loading?
  (fn [db _]
    (:loading? db)))

(reg-sub
  :users
  (fn [db _]
    (:users db)))

(reg-sub
  :users-count
  :<- [:users]
  (fn [users _]
    (count users)))