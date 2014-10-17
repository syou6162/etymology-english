(ns etymology-english.search
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [cljs-http.client :as http]
   [dommy.core :as dommy]
   [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def cursor (atom 0))

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
      [:div (:root-en w)]
      [:div (:ja w)]
      [:div (:root-ja w)]])))

(defn get-alc-url [word]
  (str "http://eow.alc.co.jp/search?q=" word "&ref=sa"))

(defn show-answer-check! [elem]
  (let [result (->> @words
                    (map
                     (fn [w]
                       [:div
                        (str (:en w) "," (if (:learned? w) "+" "-"))])))]
    (dommy/replace-contents! elem result)))

(defn operate-view!
  "Get the text value from #todo-input and add it as a new
  paragraph in #todos-div."
  [evt]
  (let [word-div (sel1 :#word)
        answer-check-div (sel1 :#answer-check)]
    (let [k (.-keyCode evt)]
      (cond (or (= k 74) (= k 75))
            (let [w (nth @words (next-cursor evt))]
              (dommy/replace-contents!
               word-div
               (if (get-in @words [@cursor :learned?])
                 (show-answer! word-div @cursor)
                 [:div [:div (:en w)]])))

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
   (->> (<! (http/get "http://localhost:8080/load-words"))
        (:body)
        (mapv (fn [w] (assoc w :learned? false)))
        (reset! words))))

(dommy/listen! (sel1 :body) :keyup operate-view!)

(.addEventListener js/window "load" load-words!)
