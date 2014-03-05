(ns etymology-english.core
  (:use [clojure.java.shell :only [sh]])
  (:require [clj-time.core :as time]))

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
   ; 39, 40
   {:en "miss / mise / mit" :ja "送る" :examples [{:en "dismiss", :ja "解散させる、退ける"}]}
   ; 41
   {:en "mode" :ja "型、尺度" :examples [{:en "remodel", :ja "建て替える、形を直す"}]}
   ; 42
   {:en "mot / mob / mov" :ja "動く" :examples [{:en "emotion", :ja "感情、情緒"}]}
   ; 43
   {:en "na(n)t / nai" :ja "生まれる" :examples [{:en "pregnant", :ja "妊娠している"}]}
   ; 44
   {:en "nom(in) / onym / noun" :ja "名前、伝える" :examples [{:en "pronounce", :ja "発音する"}]}
   ; 45
   {:en "ord(er)" :ja "命令、順序" :examples [{:en "disorder", :ja "無秩序、混乱、障害"}]}
   ; 46
   {:en "part" :ja "分ける、部分" :examples [{:en "partial", :ja "部分的な、えこひいきする、とても好きな"}]}
   ; 47
   {:en "pass / pace" :ja "歩、通り過ぎる" :examples [{:en "pastime", :ja "娯楽、気晴らし"}]}
   ; 48
   {:en "path / pass" :ja "感じる、苦しむ" :examples [{:en "antipathy", :ja "反感、嫌悪"}]}
   ; 49
   {:en "ped(e) / pod / pus" :ja "足" :examples [{:en "peddle", :ja "行商する、売り歩く"}]}
   ; 50
   {:en "pel / puls" :ja "押す、打つ" :examples [{:en "compel", :ja "強いる、余儀なくさせる"}]}
   ; 51
   {:en "pend" :ja "垂れる、さげる" :examples [{:en "depend", :ja "頼る、依存する"}]}
   ; 52
   {:en "pend / pens" :ja "(吊るして)計る、支払う" :examples [{:en "dispence", :ja "分配する、…なしですます"}]}
   ; 53
   {:en "ple / pli / ply" :ja "(折り)重なる、(折り)重ねる" :examples [{:en "complicated", :ja "複雑な、難しい"}]}
   ; 54
   {:en "popul / public / dem" :ja "人、人々" :examples [{:en "democracy", :ja "民主主義、民主政治"}]}
   ; 55
   {:en "port" :ja "運ぶ、港" :examples [{:en "export", :ja "輸出する"}]}
   ; 56, 57
   {:en "pose / posit" :ja "置く、止まる" :examples [{:en "purpose", :ja "目的、意図"}]}
   ; 58
   {:en "posit / pone" :ja "置く" :examples [{:en "postpone", :ja "延期する"}]}
   ; 59
   {:en "press" :ja "押す" :examples [{:en "depress", :ja "低迷させる、落胆させる、弱める"}]}
   ; 60
   {:en "pri(n) / pri(m)" :ja "一番目の、一つの" :examples [{:en "premier", :ja "首相、総理大臣"}]}
   ; 61
   {:en "pris(e) / pre(hend)" :ja "つかむ" :examples [{:en "imprison", :ja "投獄する"}]}
   ; 62
   {:en "quiz / quire / quest" :ja "求める、探る" :examples [{:en "conquest", :ja "征服"}]}
   ; 63
   {:en "sent / sense" :ja "感じる" :examples [{:en "consent", :ja "同意する"}]}
   ; 64
   {:en "sent / est / ess" :ja "ある、いる" :examples [{:en "interest", :ja "利害、利益、利子、興味、関心、趣味"}]}
   ; 65
   {:en "serv" :ja "保つ、使える、役立つ" :examples [{:en "conserve", :ja "保存する、大切に使う"}]}
   ; 66
   {:en "sid(e) / sess / sed" :ja "座る" :examples [{:en "preside", :ja "(議長、司会を)務める、統括する"}]}
   ; 67
   {:en "sign" :ja "印" :examples [{:en "signify", :ja "示す、意味する"}]}
   ; 68
   {:en "simi(l) / seem / sem" :ja "同じ、似ている" :examples [{:en "assemble", :ja "集まる、集める、組み立てる"}]}
   ; 69
   {:en "sist" :ja "立つ" :examples [{:en "exist", :ja "存在する、生存する"}]}
   ; 70
   {:en "sol" :ja "1番目の、一つの、太陽" :examples [{:en "solar", :ja "太陽の"}]}
   ; 71
   {:en "spec / spic / spis" :ja "見る" :examples [{:en "despiss", :ja "軽蔑する"}]}
   ; 72, 73
   {:en "spect" :ja "見る" :examples [{:en "spectator", :ja "観客"}]}
   ; 74
   {:en "stand / stant / stance" :ja "立つ、耐える" :examples [{:en "standoffish", :ja "よそよそしい、無愛想な"}]}
   ; 75
   {:en "sta(a)" :ja "立つ、建てる" :examples [{:en "obstacle", :ja "障害(物)、邪魔(物)"}]}
   ; 76
   {:en "stat" :ja "立つ" :examples [{:en "stateman", :ja "政治家"}]}
   ; 77
   {:en "stick / sting / stim" :ja "刺す、突く" :examples [{:en "distinguish", :ja "区別する"}]}
   ; 78
   {:en "stitute" :ja "立つ" :examples [{:en "destitute", :ja "貧困した、極貧の"}]}
   ; 79
   {:en "string / strict" :ja "結ぶ、縛る、引く、伸ばす" :examples [{:en "district", :ja "地方、地域、地区"}]}
   ; 80
   {:en "strai(n) / stre" :ja "結ぶ、縛る、引く、伸ばす" :examples [{:en "distress", :ja "苦悩、悩み、苦境"}]}
   ; 81
   {:en "str(uct)" :ja "建てる、積む" :examples [{:en "construct", :ja "建設する"}]}
   ; 82
   {:en "tact / tang / tag" :ja "触れる" :examples [{:en "intact", :ja "損なわれない、無傷の、そのままの"}]}
   ; 83
   {:en "tail / cide / cis(e)" :ja "切る" :examples [{:en "detail", :ja "詳細、細部"}]}
   ; 84
   {:en "tain" :ja "保つ" :examples [{:en "obtain", :ja "得る、獲得する"}]}
   ; 85, 86
   {:en "tens / tend / tent" :ja "伸ばす、張る" :examples [{:en "tendency", :ja "傾向"}]}
   ; 87
   {:en "tin / ten(e)" :ja "保つ、続く" :examples [{:en "content", :ja "中身、内容"}]}
   ; 88
   {:en "ton(e) / tun / so(u)n" :ja "音、雷" :examples [{:en "monotonous", :ja "単調な、つまらない"}]}
   ; 89, 90
   {:en "tract / tra(i)" :ja "引く" :examples [{:en "abstract", :ja "抽象的な、難解な、摘要"}]}
   ; 91
   {:en "vac / vast / va" :ja "空の" :examples [{:en "vacant", :ja "空いている、空席の"}]}
   ; 92
   {:en "vest / veil / vel" :ja "覆う、包む、着せる" :examples [{:en "invest", :ja "与える、投資する"}]}
   ; 93, 94
   {:en "verse" :ja "回す、回る、向ける、向く" :examples [{:en "converse", :ja "会話する"}]}
   ; 95
   {:en "vert" :ja "回す、回る、向ける、向く" :examples [{:en "extrovert", :ja "社交的な人、社交的な"}]}
   ; 96
   {:en "via / voy" :ja "道、進む" :examples [{:en "deviate", :ja "それる、逸脱する"}]}
   ; 97
   {:en "vis(e)" :ja "見る" :examples [{:en "revise", :ja "見直す、修正する、校正する、改訂する"}]}
   ; 98
   {:en "view / v(e)y" :ja "見る" :examples [{:en "envy", :ja "うらやましく思う、ねたみ"}]}
   ; 99
   {:en "viv" :ja "生きる、生命" :examples [{:en "survive", :ja "生き残る"}]}
   ; 100
   {:en "vent / ven(e)" :ja "来る" :examples [{:en "prevent", :ja "妨げる、防止する"}]}
   ])

(def suffix
  [{:en "ate / ize / ise / (i)fy / en / ish / er / le"
    :ja "…にする、…化する" :examples [{:en "terminate", :ja "終わらせる"}]}
   {:en "ful / ous / y"
    :ja "…の多い、…の性質がある" :examples [{:en "wonderful", :ja "すばらしい、驚くべき"}]}
   {:en "able / ible"
    :ja "…できる" :examples [{:en "etable", :ja "(どうにか)食べられる"}]}
   {:en "ish / ly / like / some / esque / ique"
    :ja "…らしい" :examples [{:en "childish", :ja "子供っぽい"}]}
   {:en "ate / it / ed / ive / ic / al / ial / ical / ual / an / ary / ory / ant / ent / ar / ine / ile / en / id"
    :ja "…の性質を持つ、…に関する、…に属する、…の" :examples [{:en "separate", :ja "離れた、別々の"}]}
   {:en "ward"
    :ja "方向を示す接尾辞" :examples [{:en "southward", :ja "南方へ"}]}
   {:en "er / eer / or / ar / (i)an / ant / ent / ist / ee / ster"
    :ja "人を表わす接尾辞" :examples [{:en "painter", :ja "画家"}]}
   {:en "ry / ory / ary / ery"
    :ja "場所を表わす接尾辞" :examples [{:en "laboratory", :ja "研究室、実験室"}]}
   {:en "age"
    :ja "抽象名詞を作る接尾辞(状態)" :examples [{:en "marriage", :ja "結婚"}]}
   {:en "ade"
    :ja "抽象名詞を作る接尾辞(単位、状態)" :examples [{:en "decade", :ja "10年間"}]}
   {:en "dom"
    :ja "抽象名詞を作る接尾辞(状態、範囲)" :examples [{:en "kingdom", :ja "王国"}]}
   {:en "ion / tion / sion"
    :ja "抽象名詞を作る接尾辞(動作、状態、関係、結果)" :examples [{:en "reaction", :ja "反応"}]}
   {:en "ment"
    :ja "抽象名詞を作る接尾辞(動作、状態、結果、手段)" :examples [{:en "document", :ja "文書、書類"}]}
   {:en "ure"
    :ja "抽象名詞を作る接尾辞(動作、結果、手段)" :examples [{:en "culture", :ja "文化"}]}
   {:en "ics"
    :ja "抽象名詞を作る接尾辞(学問)" :examples [{:en "economics", :ja "経済学"}]}
   {:en "ery"
    :ja "抽象名詞を作る接尾辞(状態、性質、身分)" :examples [{:en "slavery", :ja "奴隷制"}]}
   {:en "hood"
    :ja "抽象名詞を作る接尾辞(状態、性質、集合)" :examples [{:en "childhood", :ja "幼少時代"}]}
   {:en "ism"
    :ja "抽象名詞を作る接尾辞(行動、状態、大系、主義、特性)" :examples [{:en "heroism", :ja "英雄的行為"}]}
   {:en "al"
    :ja "抽象名詞を作る接尾辞(動作)" :examples [{:en "arrival", :ja "到着"}]}
   {:en "ness"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "happiness", :ja "幸福"}]}
   {:en "ty"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "liberty", :ja "自由"}]}
   {:en "ard"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "coward", :ja "臆病な"}]}
   {:en "ancy / ency / cy"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "vacacy", :ja "空間、空席"}]}
   {:en "ance / ence"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "clearance", :ja "除去、片づけ"}]}
   {:en "tude"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "magnitude", :ja "大きさ"}]}
   {:en "th"
    :ja "抽象名詞を作る接尾辞(状態、性質)" :examples [{:en "warmth", :ja "暖かさ"}]}
   {:en "let / et / ette / icle / en / le / y / ie"
    :ja "小さいことを表わす接尾辞" :examples [{:en "starlet", :ja "小さい星"}]}
   ])

(def ^:dynamic *column-size* "\\begin{longtable}{|p{9em}|p{6em}|p{6em}|p{6em}|}")

(defn print-tex [coll]
  (println "{\\footnotesize")
  (println *column-size*)
  (println "\\hline")
  (doseq [p (apply concat (repeat 1 coll))]
    (println (str (:en p) " & " (:ja p) " & "
                  (-> p :examples first :en)
                  " & "
                  (-> p :examples first :ja)
                  "\\\\ \\hline")))
  (println "\\end{longtable}}"))

(def ^:dynamic *max-str-size* 7)

(defn short-text [word']
  (let [word (-> word'
                 (clojure.string/replace " / " ",")
                 (clojure.string/replace "、" ","))]
    (subs word 0 (min (count word) *max-str-size*))))

(let [cnt (atom 0)
      year (time/year (time/now))
      month (time/month (time/now))
      day (time/day (time/now))
      filename (str "logs/" year "-" month "-" day ".csv")
      appeared? (atom #{})
      get-next-root (fn [coll]
                      (let [root (rand-nth coll)]
                        (if (contains? @appeared? root)
                          (recur coll)
                          (do
                            (swap! appeared? conj root)
                            root))))]
  (spit filename "")
  (defn print-checklist [max-size coll]
    (println *column-size*)
    (println "\\hline")
    (doseq [idx (range 1 (inc max-size))]
      (let [root (get-next-root coll)
            word (rand-nth (:examples root))]
        (spit filename (str (:en word) ",+\n") :append true)
        (println (str
                  (short-text (:ja word))
                  " & "
                  (binding [*max-str-size* 14]
                    (short-text (:en root)))
                  " & "
                  (short-text (:ja root))
                  " & "
                  (swap! cnt inc)
                  " & "
                  (:en word)
                  " & "
                  " \\\\ \\hline"))))
    (println "\\end{tabular}
%\\end{flushright}
%\\end{flushleft}
%\\end{center}
\\end{table}")))

(defn print-empty-table [max-size]
  (println *column-size*)
  (println "\\hline")
  (doseq [idx (range 1 (inc max-size))]
    (let []
      (println (str " & & & & & \\\\"))))
  (println "\\hline \\end{tabular} \\end{table}"))

(defn -main [& args]
  (binding [*out* (java.io.FileWriter. "prefix.tex")]
    (print-tex prefix))
  (binding [*out* (java.io.FileWriter. "root.tex")
            *column-size* "\\begin{longtable}{|p{9em}|p{6em}|p{5em}|p{7em}|}"]
    (print-tex root))
  (binding [*out* (java.io.FileWriter. "suffix.tex")
            *column-size* "\\begin{longtable}{|p{9em}|p{8em}|p{5em}|p{5em}|}"]
    (print-tex suffix))

  (binding [*out* (java.io.FileWriter. "checklist_body.tex")]
    (binding [*column-size* "\\begin{table}
\\begin{tabular}{|p{7em}|p{6em}|p{6em}|p{2em}|p{5em}|p{7em}|}"]
     (print-checklist 35 root)
     (print-checklist 15 root)
)
    (binding [*column-size* "\\begin{table}
\\begin{tabular}{|p{6em}p{6em}p{6em}p{2em}p{5em}p{8em}|}"]
      (print-empty-table 20)))
  (println (sh "omake"))
  (shutdown-agents))
