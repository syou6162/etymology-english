(ns etymology-english.json-server
  (:use [ring.adapter.jetty :only (run-jetty)])
  (:require [compojure.handler :as handler])
  (:use [compojure.core :only (defroutes context GET PUT POST)])
  (:use [compojure.route :only (not-found files resources)])
  (:use [hiccup.page-helpers :only (html5)])
  (:require [clojure.data.json :as json])
  (:use [etymology-english.core :only (root)]))

(import '(org.atilika.kuromoji Tokenizer Token))

(def ^:dynamic *refresh* 300)
(def ^:dynamic *vibrate* 0)
(def ^:dynamic *font* 4)
(def ^:dynamic *theme* 0)

(def logs-dir "logs")

(defn get-words []
  (let [s (->> (vec (.list (java.io.File. logs-dir)))
               (map #(str logs-dir "/" %))
               (map slurp)
               (clojure.string/join "\n"))
        ;; 全単語を登場させるためにディフォルトで間違いのカウントを1つ追加しておく
        default-count (->> root
                           (map (fn [item]
                                  (->> (:examples item)
                                       (map (fn [w] [(:en w) "-"])))))
                           (reduce into []))
        words (->> (clojure.string/split s #"(\r)?\n")
                   (remove (fn [line] (= line "")))
                   (map #(clojure.string/split % #","))
                   (into default-count)
                   (group-by first)
                   (map
                    (fn [[w v]]
                      (let [result (group-by second v)
                            pos (count (get result "+" []))
                            neg (count (get result "-" []))
                            total (+ pos neg)]
                        [w (/ neg total)])))
                   (sort-by second >)
                   (map first))]
    words))

(defn get-word-stat-map []
  (let [s (->> (vec (.list (java.io.File. logs-dir)))
               (map #(str logs-dir "/" %))
               (map slurp)
               (clojure.string/join "\n"))
        ;; 全単語を登場させるためにディフォルトで間違いのカウントを1つ追加しておく
        default-count (->> root
                           (map (fn [item]
                                  (->> (:examples item)
                                       (map (fn [w] [(:en w) "-"])))))
                           (reduce into []))
        result (->> (clojure.string/split s #"(\r)?\n")
                    (remove (fn [line] (= line "")))
                    (map #(clojure.string/split % #","))
                    (into default-count)
                    (group-by first)
                    (map
                     (fn [[w v]]
                       (let [result (group-by second v)
                             pos (count (get result "+" []))
                             neg (count (get result "-" []))
                             total (+ pos neg)]
                         {w {:pos-cnt pos
                             :neg-cnt neg
                             :rate (/ pos total)}})))
                    (reduce merge {}))]
    result))

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

(let [tokenizer (.. Tokenizer builder build)
      line2tokens #(.tokenize tokenizer %)]
  (defn get-reading [line]
    (->> (line2tokens line)
         (map (fn [^Token w] (.getReading w)))
         (clojure.string/join ""))))

(defn json-content []
  {"content" (->> (get-words)
                  (map-indexed
                   (fn [idx w]
                     (let [reading (-> word-to-root-info
                                       (get-in [w :ja])
                                       (get-reading))]
                       (str idx ": " w "\n" reading))))
                  (take 15)
                  (clojure.string/join "\n"))
   "refresh" *refresh*
   "vibrate" *vibrate*
   "font" *font*
   "theme" *theme*})

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
            (clojure.string/join "<br />")))
  (GET "/word-stat-map" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (->> (get-word-stat-map)
                   (json/write-str))})
  (GET "/list-by-root" []
       (let [stat-map (get-word-stat-map)
             result (->> root
                         (map
                          (fn [w]
                            [:li
                             (str (:en w) " (" (:ja w) ")")
                             [:ul
                              (->> (:examples w)
                                   (map
                                    (fn [item]
                                      [:li (:en item) " (" (:ja item) ") => "
                                       (let [w (:en item)]
                                         (str
                                          "(total: " (int (* 100 (-> (get stat-map w) :rate))) "%"
                                          ", correct: " (-> (get stat-map w) :pos-cnt)
                                          ", wrong: " (-> (get stat-map w) :neg-cnt) ")"))])))]])))]
         (html5
          [:body
           [:ul result]]))))

(def app (handler/site routes))

(defn -main []
  ; heroku向けのport取得
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
