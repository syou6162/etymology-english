(ns etymology-english.learn
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [cljs-http.client :as http]
   [dommy.core :as dommy]
   [cljs.core.async :refer [<! timeout]]
   [goog.string :as gstring]
   [goog.string.format :as gformat]))

(enable-console-print!)

(def cursor (atom -1))

(def words (atom []))

(defn next-cursor [evt]
  (let [default @cursor]
    (cond (= 74 (.-keyCode evt)) ;; j
          (if (< @cursor (dec (count @words)))
            (swap! cursor inc)
            default)

          (= 75 (.-keyCode evt)) ;; k
          (if (< 0 @cursor)
            (swap! cursor dec)
            default)

          :else default)))

(defn show-answer! [elem word-idx]
  (let [w (nth @words word-idx)]
    (dommy/replace-contents!
     elem
     [:div
      [:div (:en w)]
      [:div (:ja w)]
      [:div (str (:root-en w) ": " (:root-ja w))]])))

(defn get-alc-url [word]
  (str "http://eow.alc.co.jp/search?q=" word "&ref=sa"))

(defn show-answer-check! [elem]
  (let [done "done"
        result (->> @words
                    (map
                     (fn [w]
                       [:div
                        (str (:en w) "," (if (:learned? w) "+" "-"))])))
        [front' end] (split-at (inc @cursor) result)
        front [(-> [:div front']
                   (dommy/set-attr! :id done))]
        select-done-words! (fn []
                             (let [elem (. js/document (getElementById done))
                                   rng (. js/document (createRange))]
                               (.removeAllRanges (. js/window getSelection))
                               (. rng (selectNodeContents elem))
                               (.addRange (. js/window getSelection) rng)))]
    (->> (concat front end)
         (dommy/replace-contents! elem))
    (select-done-words!)))

(defn operate-view! [evt]
  (let [word-div (sel1 :#word)
        answer-check-div (sel1 :#answer-check)]
    (let [k (.-keyCode evt)]
      (cond (or (= k 74) (= k 75))
            (let [w (nth @words (next-cursor evt))]
              (if (get-in @words [@cursor :learned?])
                (show-answer! word-div @cursor)
                (dommy/replace-contents!
                 word-div [:div [:div (:en w)]])))

            (= k 79) ;; o
            (show-answer! word-div @cursor)

            (= k 80) ;; p
            (do
              (show-answer! word-div @cursor)
              (swap! words update-in [@cursor :learned?] not))

            (= k 81) ;; q
            (show-answer-check! answer-check-div)

            (= k 86) ;; v
            (let [url (get-alc-url (:en (nth @words @cursor)))]
              (.open js/window url)))
      (let [result (str @cursor " / " (count @words))]
        (dommy/set-text! (sel1 :#progress) result))
      (if (get-in @words [@cursor :learned?])
        (dommy/set-style! word-div :color "red")
        (dommy/set-style! word-div :color "black")))))

(defn load-words! []
  (go
   (let [difficult-num 20
         total-num 50
         result' (->> (<! (http/get "./load-words"))
                      (:body)
                      (mapv (fn [w] (assoc w :learned? false))))
         [difficult easy] (split-at difficult-num result')
         [easy-first rest] (split-at (- total-num difficult-num) (shuffle easy))
         head (shuffle (concat difficult easy-first))
         result (->> (into head rest)
                     (vec))]
     (reset! words result))))

(dommy/listen! (sel1 :body) :keyup operate-view!)

(.addEventListener js/window "load" load-words!)

(let [start (js/Date.)]
  (defn show-time-progress! []
    (let [time-progress-div (sel1 :#time-progress)
          pad-zero (fn [x] (if (> 10 x) (str "0" x) x))]
      (go
       (loop []
         (let [now (js/Date.)
               tmp (/ (- (.getTime now) (.getTime start)) 1000)
               hour (pad-zero (js/parseInt (/ tmp 3600)))
               minute (pad-zero (js/parseInt (mod (/ tmp 60) 60)))
               second (pad-zero (js/parseInt (mod tmp 60)))
               result (str hour ":" minute ":" second)]
           (dommy/set-text! time-progress-div result))
         (<! (timeout 1000))
         (recur))))))

(set! (.-onload js/window) show-time-progress!)
