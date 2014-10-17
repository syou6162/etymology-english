(ns etymology-english.json-server
  (:use [ring.adapter.jetty :only (run-jetty)])
  (:require [compojure.handler :as handler])
  (:use [compojure.core :only (defroutes context GET PUT POST)])
  (:use [compojure.route :only (not-found files resources)])
  (:require [clojure.data.json :as json])
  (:use [etymology-english.core :only (root)]))

(def ^:dynamic *refresh* 300)
(def ^:dynamic *vibrate* 0)
(def ^:dynamic *font* 8)
(def ^:dynamic *theme* 0)

(def logs-dir "logs")

(defn get-words []
  (let [s (->> (vec (.list (java.io.File. logs-dir)))
               (map #(str logs-dir "/" %))
               (map slurp)
               (clojure.string/join "\n"))
        words (->> (clojure.string/split s #"\n")
                   (map #(clojure.string/split % #","))
                   (filter (fn [[w correct?]] (= "-" correct?)))
                   (map first)
                   (frequencies)
                   (sort-by second >)
                   (map first))]
    words))

(defn json-content []
  {"content" (->> (get-words)
                  (map-indexed (fn [idx w] (str idx ": " w)))
                  (clojure.string/join "\n"))
   "refresh" *refresh*
   "vibrate" *vibrate*
   "font" *font*
   "theme" *theme*})

(def word-to-root-info
  (->> root
       (map (fn [item]
              (->> (:examples item)
                   (map
                    (fn [w]
                      [(:en w)
                       {:root-en (:en item)
                        :root-ja (:ja item)
                        :en (:en w)
                        :ja (:ja w)}])))))
       (reduce into [])
       (into {})))

(defroutes routes
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (json/write-str (json-content))})
  (GET "/load-words" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (->> (get-words)
                   (map word-to-root-info)
                   (json/write-str))})
  (resources "/")
  (GET "/list" []
       (->> (get-words)
            (map #(get word-to-root-info %))
            (map-indexed
             (fn [idx item]
               (str idx ": " (:en item) "\t" (:ja item) "\t"
                    (:root-en item) "\t" (:root-ja item))))
            (clojure.string/join "<br />"))))

(def app (handler/site routes))

(defn -main []
  ; heroku向けのport取得
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
