(ns etymology-english.core
  (:use [clojure.java.shell :only [sh]]))

(def prefix
  [{:en "en" :ja "にする" :examples [{:en "enrich", :ja "豊かにする"}]}
   {:en "co / con / com / col / cor" :ja "共に" :examples [{:en "coworker", :ja "同僚"}]}
   {:en "syn / sym" :ja "共に" :examples [{:en "synchronize", :ja "同時に起きる"}]}
   {:en "a" :ja "方へ" :examples [{:en "abroad", :ja "海外に"}]}
   {:en "ad" :ja "方へ" :examples [{:en "adverb", :ja "副詞"}]}
   {:en "a" :ja "ない、離れて" :examples [{:en "atom", :ja "原子"}]}
   {:en "ab" :ja "離れて" :examples [{:en "abhor", :ja "忌み嫌う"}]}
   {:en "de" :ja "離れて" :examples [{:en "derail", :ja "脱線させる"}]}
   {:en "de" :ja "下に" :examples [{:en "decay", :ja "腐敗する、衰える"}]}
   {:en "de" :ja "完全に、すっかり" :examples [{:en "demonstrate", :ja "証言する、説明する"}]}
   {:en "dis" :ja "ない" :examples [{:en "disable", :ja "できなくさせる"}]}
   {:en "di" :ja "離れて" :examples [{:en "divide", :ja "分ける"}]}
   {:en "se" :ja "離れて、ない" :examples [{:en "security", :ja "安全"}]}
   {:en "dia" :ja "通して、すっかり" :examples [{:en "dialog", :ja "対話"}]}
   {:en "per" :ja "通して、すっかり" :examples [{:en "persecute", :ja "追害する"}]}
   {:en "in" :ja "ない" :examples [{:en "incorrect", :ja "正しくない"}]}
   {:en "im" :ja "ない" :examples [{:en "impossible", :ja "不可能な"}]}
   {:en "un" :ja "ない" :examples [{:en "unable", :ja "できない"}]}
   {:en "un" :ja "元の状態に戻す" :examples [{:en "unlock", :ja "解錠する"}]}
   {:en "non" :ja "ない" :examples [{:en "nonsense", :ja "意味のない"}]}
   {:en "ob" :ja "反対に、対して" :examples [{:en "obstacle", :ja "障害"}]}
   {:en "ex / e" :ja "外に" :examples [{:en "exhale", :ja "息を吐き出す"}]}
   {:en "extra / extro" :ja "範囲害の" :examples [{:en "extra", :ja "余分な"}]}
   {:en "sub / su" :ja "下に" :examples [{:en "subway", :ja "地下鉄、地下道"}]}
   {:en "sur" :ja "上に、越えて" :examples [{:en "surface", :ja "表面"}]}
   {:en "super" :ja "上に、越えて" :examples [{:en "superior", :ja "より優れた"}]}
   {:en "ultra" :ja "越えて" :examples [{:en "ultrasonic", :ja "超音速の"}]}
   {:en "em" :ja "中へ" :examples [{:en "embark", :ja "乗船する、乗り出す"}]}
   {:en "inter" :ja "間に" :examples [{:en "international", :ja "国際的な"}]}
   {:en "intro / intra" :ja "中に" :examples [{:en "introspect", :ja "反省する"}]}
   {:en "contra / contro / counter" :ja "反対に" :examples [{:en "contrary", :ja "反対の"}]}
   {:en "ambi / ambu" :ja "周りに" :examples [{:en "ambiguous", :ja "あいまいな"}]}
   {:en "circum / circ" :ja "回りに" :examples [{:en "circumstance", :ja "環境、周囲"}]}
   {:en "peri" :ja "回りに" :examples [{:en "periodic", :ja "周期的な"}]}
   {:en "para" :ja "側に" :examples [{:en "paralyze", :ja "まひさせる"}]}
   {:en "pro" :ja "前に" :examples [{:en "professor", :ja "教授"}]}
   {:en "pre" :ja "前に" :examples [{:en "preschool", :ja "保育所"}]}
   {:en "anti" :ja "反対して" :examples [{:en "antisocial", :ja "反社会的な"}]}
   {:en "post" :ja "後に" :examples [{:en "postwar", :ja "戦後の"}]}
   {:en "re" :ja "再び" :examples [{:en "rebuild", :ja "再建する"}]}
   {:en "re" :ja "後ろに、離れて、逆らって" :examples [{:en "reflect", :ja "反射する"}]}
   {:en "re" :ja "しっかり、完全に" :examples [{:en "regard", :ja "見なす、思う"}]}
   {:en "com" :ja "すっかり、完全に" :examples [{:en "complete", :ja "完全な"}]}
   {:en "trans" :ja "越えて" :examples [{:en "transplant", :ja "移植する"}]}
   {:en "mono / mon" :ja "1つ" :examples [{:en "monorail", :ja "モノレール"}]}
   {:en "uni / un" :ja "1つ" :examples [{:en "unite", :ja "結合する"}]}
   {:en "semi / hemi" :ja "半分" :examples [{:en "semicircle", :ja "半円"}]}
   {:en "bi / bis / bin / di" :ja "2つ" :examples [{:en "bicycle", :ja "自転車"}]}
   {:en "du / duo" :ja "2つ" :examples [{:en "dual", :ja "二重の"}]}
   {:en "tri" :ja "3つ" :examples [{:en "triangle", :ja "三角"}]}
   {:en "quart / quadr" :ja "4つ" :examples [{:en "quarter", :ja "4分の1"}]}
   {:en "penta" :ja "5つ" :examples [{:en "pentagon", :ja "五角形"}]}
   {:en "hexa" :ja "6つ" :examples [{:en "hexagon", :ja "六角形"}]}
   {:en "hepta" :ja "7つ" :examples [{:en "heptagon", :ja "七角形"}]}
   {:en "oct" :ja "8つ" :examples [{:en "octopus", :ja "タコ"}]}
   {:en "nona" :ja "9つ" :examples [{:en "nonagon", :ja "九角形"}]}
   {:en "deca / deci / dim" :ja "10つ" :examples [{:en "decade", :ja "10年間の"}]}
   {:en "cent / centi" :ja "100の" :examples [{:en "century", :ja "100年、1世紀"}]}
   {:en "mill" :ja "1000の" :examples [{:en "mile", :ja "マイル"}]}
   {:en "multi" :ja "たくさん" :examples [{:en "multinational", :ja "多国籍の"}]}
   {:en "omni" :ja "すべて" :examples [{:en "omnibus", :ja "バス"}]}])

(def root
  [; 1
   {:en "ali / alter / else" :ja "別の、他の" :examples [{:en "alien", :ja "外国の、異質な"}]}
   ; 2
   {:en "ann / enn" :ja "年" :examples [{:en "annuity", :ja "年金(制度)"}]}
   ; 3
   {:en "aster / astro / stella" :ja "星" :examples [{:en "disaster", :ja "大惨事、災害"}]}
   ; 4
   {:en "cap" :ja "頭" :examples [{:en "escape", :ja "逃げる、逃れる"}]}
   ; 5, 6
   {:en "cede / cess" :ja "進む、行く、譲る" :examples [{:en "necessity", :ja "必要性、必需品"}]}
   ; 7
   {:en "ceive, cept" :ja "つかむ" :examples [{:en "deceive", :ja "だます"}]}
   ; 8
   {:en "close / clude" :ja "閉じる" :examples [{:en "disclose", :ja "発表する、暴露する"}]}
   ; 9
   {:en "cor(d)" :ja "心" :examples [{:en "core", :ja "芯、中心部"}]}
   ; 10
   {:en "cre / cru" :ja "生む、成長する" :examples [{:en "increase", :ja "増加する、増加"}]}
   ; 11
   {:en "cro / cru" :ja "十字、曲げる" :examples [{:en "crossroad", :ja "十字路、交差点、岐路"}]}
   ; 12
   {:en "cur / cour / cor" :ja "流れる、走る" :examples [{:en "excursion", :ja "遠足、小旅行"}]}
   ; 13
   {:en "dict" :ja "話す、言う、示す" :examples [{:en "contradict", :ja "矛盾する、否定する"}]}
   ; 14
   {:en "duct" :ja "導く" :examples [{:en "product", :ja "製品、生産品"}]}
   ; 15
   {:en "duce" :ja "導く" :examples [{:en "produce", :ja "生産する、取り出す"}]}
   ; 16
   {:en "equ" :ja "平らな、等しい" :examples [{:en "equator", :ja "赤道"}]}
   ; 17
   {:en "fa(m) / fan(n) / phe" :ja "話す" :examples [{:en "infantry", :ja "歩兵(隊)"}]}
   ; 18
   {:en "fac(t)" :ja "作る、する" :examples [{:en "facile", :ja "容易な、簡単な、器用な"}]}
   ; 19
   {:en "fect" :ja "作る、する" :examples [{:en "defect", :ja "欠点、欠陥、傷"}]}
   ; 20, 21
   {:en "fer" :ja "運ぶ、産む、耐える" :examples [{:en "offer", :ja "申し出、誘い"}]}
   ; 22
   {:en "fic" :ja "作る、する" :examples [{:en "deficient", :ja "不足する"}]}
   ; 23
   {:en "fin" :ja "終わる、境界" :examples [{:en "fine", :ja "罰金を科す、罰金、すばらしい、元気な、細かい"}]}
   ; 24
   {:en "flo / flu" :ja "流れる" :examples [{:en "influence", :ja "影響(力)"}]}
   ; 25
   {:en "form" :ja "形" :examples [{:en "reform", :ja "改革する、改善する"}]}
   ; 26
   {:en "fuse / fut" :ja "注ぐ、融ける" :examples [{:en "refuse", :ja "断わる、拒絶する"}]}
   ; 27, 28
   {:en "gen" :ja "生まれる、種" :examples [{:en "genetic", :ja "遺伝子の"}]}
   ; 29
   {:en "guard / war(d)" :ja "見守る" :examples [{:en "award", :ja "与える、裁定する、賞品、賞金、裁定額"}]}
   ; 30
   {:en "it" :ja "行く" :examples [{:en "exit", :ja "出口、退去する、立ち去る"}]}
   ; 31
   {:en "ject / jet" :ja "投げる" :examples [{:en "reject", :ja "拒否する、拒絶する"}]}
   ; 32
   {:en "just / jur / jud" :ja "正しい、法" :examples [{:en "adjust", :ja "調整する、適合させる"}]}
   ; 33
   {:en "lea(g) / li(g) / ly" :ja "結ぶ" :examples [{:en "religion", :ja "宗教"}]}
   ; 34
   {:en "lect / leg / lig" :ja "集める、選ぶ" :examples [{:en "recollect", :ja "思い出す、記憶する"}]}
   ; 35
   {:en "man(i) / man(u)" :ja "手" :examples [{:en "manuscript", :ja "原稿、写本、手紙"}]}
   ; 36
   {:en "med / mid" :ja "中間" :examples [{:en "mediate", :ja "仲介する、調停する"}]}
   ; 37
   {:en "memo / min" :ja "記憶、思い起こす" :examples [{:en "immemorial", :ja "太古の、大昔の"}]}
   ; 38
   {:en "mini" :ja "小さい" :examples [{:en "minor", :ja "小さい方の、重要でない、未成年者"}]}
   ])

(defn print-tex [coll]
  (println "{\\footnotesize")
  (println "\\begin{longtable}{|p{9em}|p{6em}|p{6em}|p{6em}|}")
  (println "\\hline")
  (doseq [p (apply concat (repeat 1 coll))]
    (println (str (:en p) " & " (:ja p) " & "
                  (-> p :examples first :en)
                  " & "
                  (-> p :examples first :ja)
                  "\\\\ \\hline")))
  (println "\\end{longtable}}"))

(defn -main [& args]
  (binding [*out* (java.io.FileWriter. "prefix.tex")]
    (print-tex prefix))
  (binding [*out* (java.io.FileWriter. "root.tex")]
    (print-tex root))
  (println (sh "omake"))
  (shutdown-agents))
