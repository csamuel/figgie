(ns ^:figwheel-no-load figgie.dev
  (:require [figgie.core :as core]
            [figwheel.client :as figwheel :include-macros true]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install! [:formatters :hints :async])

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback core/mount-root)

(core/init!)
