(ns monitor.core
  (:require [clojure.pprint :as pp]
            [clojure.core.async :as a :refer [<! go go-loop chan put! timeout]]
            [clj-http.client :as client]
            [postal.core :refer [send-message]])
  (:import (java.util Date))
  (:gen-class))

(def running? (atom true))

(defn pretty-print [col]
  (with-out-str (pp/pprint col)))

(defn send-mail [status response recipient]
  (send-message {:host "smtp.gmail.com"
                 :user "user"
                 :pass "pass"
                 :ssl true}
                {:from "i@mgro.ot"
                 :to recipient
                 :subject "[D-BAS Monitoring] D-BAS unreachable"
                 :body [:alternative {:type "text/html"
                                      :content (str "<html><head>D-BAS Monitoring</head><body>
                                                    <p>Server responded with http status code: <strong>" status "</strong></p>
                                                    <pre><code>" (pretty-print response) "</code></pre>
                                                    </body></html>")}]}))

(defn success [time]
  (do
    #_(println "success")
    (spit "logs/success.log" (str (new Date)": Everything looks fine, response time: " time " ms\n") :append true)))

(defn error [status response]
  (do
    #_(println "error")
    (spit "logs/error.log" (str (new Date)": Server unreachable " (pretty-print response) "\n") :append true)
    (send-mail status response "i@mgro.ot")))

(defn ping [url]
  (let [res (client/head url)
        status (:status res)]
    [status res]))

(defn run []
  (println "Starting loop...")
  (go-loop []
    (let [x (<! (timeout (* 15 60 1000)))  ;; ms to minutes
          [status res] (ping "https://dbas.cs.uni-duesseldorf.de")]
      (if (= 200 status)
        (success (:request-time res))
        (error status res)))
    (when @running? (recur))))
;; (run)
;; (reset! running? true)
;; (reset! running? false)

(defn -main [& arg]s
  (println "D-BAS Monitoring")
  (run))
