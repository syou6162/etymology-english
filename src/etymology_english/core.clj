(ns etymology-english.core
  (:use [clojure.java.shell :only [sh]])
  (:use [clj-time.format :only (parse formatter)])
  (:require [clj-time.core :as time])
  (:require [clojure.tools.cli :as cli]))

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
   {:en "ali / alter / else" :ja "別の、他の"
    :examples [{:en "alien", :ja "外国の、異質な"}
               {:en "alienate", :ja "疎外する、遠ざける"}
               {:en "alternate", :ja "交互にする"}
               {:en "alter", :ja "作りかえる、改造する、変化する"}
               {:en "alternative", :ja "二者択一"}
               {:en "alias", :ja "別名は"}]}
   ; 2
   {:en "ann / enn" :ja "年"
    :examples [{:en "annuity", :ja "年金(制度)"}
               {:en "perennial", :ja "多年生の、永続的な"}
               {:en "annals", :ja "年代記、記録"}
               {:en "annual", :ja "年一回の、毎年の"}
               {:en "biennial", :ja "二年に一度の"}
               {:en "centennial", :ja "百周年の"}]}
   ; 3
   {:en "aster / astro / stella" :ja "星"
    :examples [{:en "disaster", :ja "大惨事、災害"}
               {:en "astronomy", :ja "天文学"}
               {:en "constellation", :ja "星座"}
               {:en "asteroid", :ja "小惑星"}
               {:en "astrology", :ja "星占い術"}
               {:en "astronaut", :ja "宇宙飛行士"}]}
   ; 4
   {:en "cap" :ja "頭"
    :examples [{:en "escape", :ja "逃げる、逃れる"}
               {:en "capital", :ja "資本金、首都、大文字"}
               {:en "decapitate", :ja "首を切る"}
               {:en "capable", :ja "能力がある"}
               {:en "captive", :ja "捕虜"}
               {:en "capsize", :ja "転覆する"}]}
   ; 5, 6
   {:en "cede / cess" :ja "進む、行く、譲る"
    :examples [{:en "necessity", :ja "必要性、必需品"}
               {:en "ancestor", :ja "祖先、先駆者"}
               {:en "excess", :ja "過度、超過、暴飲暴食"}
               {:en "recess", :ja "休憩、休暇"}
               {:en "incessant", :ja "絶え間のない、ひっりなしの"}
               {:en "predecessor", :ja "前任者、前にあったもの"}
               {:en "succeed", :ja "後を継ぐ、相続する"}
               {:en "secede", :ja "脱退する"}
               {:en "recede", :ja "後退する、遠ざかる"}
               {:en "preceed", :ja "進む、続ける"}
               {:en "concede", :ja "認める、与える、譲る"}
               {:en "precede", :ja "先立つ、先に起こる"}]}
   ; 7
   {:en "ceive, cept" :ja "つかむ"
    :examples [{:en "deceive", :ja "だます"}
               {:en "perceive", :ja "理解する、気づく、感じる"}
               {:en "accept", :ja "受け取る、受け入れる"}
               {:en "reception", :ja "受け入れること、披露宴、受付"}
               {:en "conceive", :ja "思いつく、想像する、妊娠する"}
               {:en "except", :ja "…以外に"}]}
   ; 8
   {:en "close / clude" :ja "閉じる"
    :examples [{:en "disclose", :ja "発表する、暴露する"}
               {:en "conclude", :ja "結論を下す、決着をつける"}
               {:en "exclude", :ja "除外する、締め出す"}
               {:en "enclose", :ja "囲む、同封する"}
               {:en "include", :ja "含む"}
               {:en "secluded", :ja "人里離れた"}]}
   ; 9
   {:en "cor(d)" :ja "心"
    :examples [{:en "core", :ja "芯、中心部"}
               {:en "accord", :ja "一致、協定"}
               {:en "courage", :ja "勇気"}
               {:en "encourage", :ja "勇気づける、促す"}
               {:en "discourage", :ja "落胆させる、思いとどまらせる、阻止する"}
               {:en "cordial", :ja "心からの、思いやりのある"}]}
   ; 10
   {:en "cre / cru" :ja "生む、成長する"
    :examples [{:en "increase", :ja "増加する、増加"}
               {:en "decrease", :ja "減少する"}
               {:en "recruit", :ja "新しく入れる、採用する"}
               {:en "create", :ja "創造する"}
               {:en "crew", :ja "乗組員、乗務員"}
               {:en "accrue", :ja "(権力、利益が)生じる、増える"}]}
   ; 11
   {:en "cro / cru" :ja "十字、曲げる"
    :examples [{:en "crossroad", :ja "十字路、交差点、岐路"}
               {:en "crucial", :ja "決定的な"}
               {:en "crucify", :ja "はりつけにする、責め苦しめる"}
               {:en "cruise", :ja "巡遊する、巡航する"}
               {:en "crusade", :ja "十字軍、革命運動"}
               {:en "crouch", :ja "かがむ"}]}
   ; 12
   {:en "cur / cour / cor" :ja "流れる、走る"
    :examples [{:en "excursion", :ja "遠足、小旅行"}
               {:en "currency", :ja "通貨、流通"}
               {:en "recourse", :ja "頼みの綱、頼ること"}
               {:en "occur", :ja "起こる、発生する"}
               {:en "concur", :ja "一致する、同時に起こる"}
               {:en "recur", :ja "再発する"}]}
   ; 13
   {:en "dict" :ja "話す、言う、示す"
    :examples [{:en "contradict", :ja "矛盾する、否定する"}
               {:en "verdict", :ja "判決、判断、決定"}
               {:en "addict", :ja "中毒者"}
               {:en "predict", :ja "予言する、予測する"}
               {:en "dictate", :ja "書き取らせる、命令する"}
               {:en "indict", :ja "起訴する、非難する"}]}
   ; 14
   {:en "duct" :ja "導く"
    :examples [{:en "product", :ja "製品、生産品"}
               {:en "abduct", :ja "誘拐する"}
               {:en "induct", :ja "就任させる、任命する"}
               {:en "conduct", :ja "導く"}
               {:en "deduct", :ja "差し引く、控除する"}
               {:en "by-product", :ja "副産物、副作用"}]}
   ; 15
   {:en "duce" :ja "導く"
    :examples [{:en "produce", :ja "生産する、取り出す"}
               {:en "educate", :ja "教育する"}
               {:en "reduce", :ja "縮小する"}
               {:en "introduce", :ja "紹介する、導入する"}
               {:en "seduce", :ja "誘惑する、そそのかす"}
               {:en "induce", :ja "…する気にさせる"}]}
   ; 16
   {:en "equ" :ja "平らな、等しい"
    :examples [{:en "equator", :ja "赤道"}
               {:en "adequate", :ja "十分な、ちょうど良い、適した"}
               {:en "equivocal", :ja "紛らわしい、はっきりしない、いかがわしい"}
               {:en "equal", :ja "等しい、平等の、耐えられる"}
               {:en "equivalent", :ja "同等のもの"}
               {:en "equalize", :ja "等しくする、同一にする"}]}
   ; 17
   {:en "fa(m) / fan(n) / phe" :ja "話す"
    :examples [{:en "infantry", :ja "歩兵(隊)"}
               {:en "defame", :ja "中傷する"}
               {:en "prophet", :ja "予言者"}
               {:en "fatal", :ja "運命を決する、致命的な"}
               {:en "infant", :ja "幼児"}
               {:en "fabulous", :ja "驚くべき、すばらしい、伝説の"}]}
   ; 18
   {:en "fac(t)" :ja "作る、する"
    :examples [{:en "facile", :ja "容易な、簡単な、器用な"}
               {:en "facilitate", :ja "促進する、容易にする"}
               {:en "faction", :ja "派閥"}
               {:en "factor", :ja "要素、要因"}
               {:en "faculty", :ja "才能、機能、学部"}
               {:en "facititious", :ja "うその、わざとらしい、見せかけの"}]}
   ; 19
   {:en "fect" :ja "作る、する"
    :examples [{:en "defect", :ja "欠点、欠陥、傷"}
               {:en "effect", :ja "結果、効果"}
               {:en "affect", :ja "影響する、…のふりをする"}
               {:en "infect", :ja "伝染する、感染する"}
               {:en "confectionery", :ja "菓子店、菓子類"}
               {:en "prefecture", :ja "県、府"}]}
   ; 20, 21
   {:en "fer" :ja "運ぶ、産む、耐える"
    :examples [{:en "offer", :ja "申し出、誘い"}
               {:en "fertile", :ja "肥沃な、多産の"}
               {:en "conference", :ja "会議、相談"}
               {:en "refer", :ja "参照する、触れる"}
               {:en "defer", :ja "延ばす、延期する"}
               {:en "circumfence", :ja "円周、周囲"}
               {:en "prefer", :ja "好む、好きである"}
               {:en "differ", :ja "違う、異なる"}
               {:en "indifferent", :ja "無関心な、無頓着な、どうでもいい"}
               {:en "transfer", :ja "移動させる、移す"}
               {:en "differentiate", :ja "区別する、識別する"}]}
   ; 22
   {:en "fic" :ja "作る、する"
    :examples [{:en "deficient", :ja "不足する"}
               {:en "sacrifice", :ja "犠牲にする"}
               {:en "sufficient", :ja "十分な"}
               {:en "efficient", :ja "有能な、効率的な"}
               {:en "proficient", :ja "熟達した、堪能な"}
               {:en "artificial", :ja "人工的な、不自然な"}]}
   ; 23
   {:en "fin" :ja "終わる、境界"
    :examples [{:en "fine", :ja "罰金を科す、罰金、すばらしい、元気な、細かい"}
               {:en "finance", :ja "財源、財政"}
               {:en "infinite", :ja "無限の、計り知れない"}
               {:en "refine", :ja "精製する、洗練する"}
               {:en "define", :ja "定める、定義する"}
               {:en "confine", :ja "制限する、監禁する"}]}
   ; 24
   {:en "flo / flu" :ja "流れる"
    :examples [{:en "influence", :ja "影響(力)"}
               {:en "superfluous", :ja "十二分な、不要な"}
               {:en "fluctuate", :ja "変動する、動揺する"}
               {:en "flood", :ja "洪水、多数"}
               {:en "fluent", :ja "流暢な"}
               {:en "affluent", :ja "裕福な、豊富な"}]}
   ; 25
   {:en "form" :ja "形"
    :examples [{:en "reform", :ja "改革する、改善する"}
               {:en "inform", :ja "知らせる"}
               {:en "conform", :ja "従う、順応させる"}
               {:en "formulate", :ja "公式化する"}
               {:en "perform", :ja "実行する、演じる"}
               {:en "transform", :ja "変える"}]}
   ; 26
   {:en "fuse / fut" :ja "注ぐ、融ける"
    :examples [{:en "refuse", :ja "断わる、拒絶する"}
               {:en "profuse", :ja "豊富な、気前よく与える"}
               {:en "confuse", :ja "混同する、混乱させる"}
               {:en "transfusion", :ja "輸血、注入、移入"}
               {:en "refute", :ja "論破する、反駁する"}
               {:en "futile", :ja "無駄な、無益な"}]}
   ; 27, 28
   {:en "gen" :ja "生まれる、種"
    :examples [{:en "genetic", :ja "遺伝子の"}
               {:en "generate", :ja "生み出す、発生させる"}
               {:en "general", :ja "一般的な、全般的な"}
               {:en "generous", :ja "気前のよい、豊富な"}
               {:en "regenerate", :ja "再生する、改心させる"}
               {:en "degenerate", :ja "退化する、悪化する"}
               {:en "indigenous", :ja "固有の、原産の、生まれつきの"}
               {:en "genuine", :ja "本物の、純粋な"}
               {:en "congenital", :ja "先天的な"}
               {:en "genial", :ja "のこやかな、温和な"}
               {:en "ingenious", :ja "独創的な、巧妙な"}
               {:en "congenial", :ja "気の合った、同じ性質の"}]}
   ; 29
   {:en "guard / war(d)" :ja "見守る"
    :examples [{:en "award", :ja "与える、裁定する、賞品、賞金、裁定額"}
               {:en "reward", :ja "報いる、報酬"}
               {:en "regard", :ja "考える、評価する"}
               {:en "regardless", :ja "注意しない、無頓着な"}
               {:en "ward", :ja "病棟、区、後見"}
               {:en "warden", :ja "監視人、刑務所長"}]}
   ; 30
   {:en "it" :ja "行く"
    :examples [{:en "exit", :ja "出口、退去する、立ち去る"}
               {:en "transit", :ja "通貨、運送、輸送"}
               {:en "orbit", :ja "軌道、行路"}
               {:en "initiate", :ja "入会させる、始める"}
               {:en "ambition", :ja "野心、念願、夢"}
               {:en "obituary", :ja "死亡記事"}]}
   ; 31
   {:en "ject / jet" :ja "投げる"
    :examples [{:en "reject", :ja "拒否する、拒絶する"}
               {:en "eject", :ja "置い出す、取り出す、立ち退かせる"}
               {:en "inject", :ja "注入する、注射する"}
               {:en "subject", :ja "服従させる"}
               {:en "object", :ja "物体、対象、目的"}
               {:en "conjecture", :ja "憶測する、推測する"}]}
   ; 32
   {:en "just / jur / jud" :ja "正しい、法"
    :examples [{:en "adjust", :ja "調整する、適合させる"}
               {:en "justify", :ja "正当化する"}
               {:en "injure", :ja "傷つける"}
               {:en "justice", :ja "正義、公正、正当性"}
               {:en "jury", :ja "陪審員"}
               {:en "prejudice", :ja "偏見、先入観"}]}
   ; 33
   {:en "lea(g) / li(g) / ly" :ja "結ぶ"
    :examples [{:en "religion", :ja "宗教"}
               {:en "rely", :ja "頼る"}
               {:en "ally", :ja "同盟する、結びつく"}
               {:en "alliance", :ja "同盟、提携、協調"}
               {:en "liable", :ja "法的な責任がある、…しがちである"}
               {:en "oblige", :ja "余儀なくさせる、義務づける、恩義を施す"}]}
   ; 34
   {:en "lect / leg / lig" :ja "集める、選ぶ"
    :examples [{:en "recollect", :ja "思い出す、記憶する"}
               {:en "neglect", :ja "無視する、怠る、…し忘れる"}
               {:en "eligible", :ja "資格のある、適格の"}
               {:en "elect", :ja "(選挙で)選ぶ"}
               {:en "intellect", :ja "知性"}
               {:en "intelligence", :ja "知能、情報"}]}
   ; 35
   {:en "man(i) / man(u)" :ja "手"
    :examples [{:en "manuscript", :ja "原稿、写本、手紙"}
               {:en "manifest", :ja "明らかな"}
               {:en "manacle", :ja "手錠、手かせ、足かせ"}
               {:en "manage", :ja "経営する、何とかやり遂げる"}
               {:en "manipulate", :ja "操る、操作する"}
               {:en "manure", :ja "肥料、肥やし"}]}
   ; 36
   {:en "med / mid" :ja "中間"
    :examples [{:en "mediate", :ja "仲介する、調停する"}
               {:en "intermediate", :ja "中間の、中級の"}
               {:en "immediate", :ja "即座の、直接の"}
               {:en "mean", :ja "平均の、並の"}
               {:en "medieval", :ja "中世の、旧式の"}
               {:en "mediocre", :ja "並の、二流の"}]}
   ; 37
   {:en "memo / min" :ja "記憶、思い起こす"
    :examples [{:en "immemorial", :ja "太古の、大昔の"}
               {:en "commemorate", :ja "記念する、祝う"}
               {:en "reminiscent", :ja "思い出させる、しのばせる"}
               {:en "memorize", :ja "暗記する、覚える"}
               {:en "remember", :ja "思い出す、覚えている"}
               {:en "remind", :ja "思い出させる、気づかせる"}]}
   ; 38
   {:en "mini" :ja "小さい"
    :examples [{:en "minor", :ja "小さい方の、重要でない、未成年者"}
               {:en "diminish", :ja "減少する、小さくする"}
               {:en "minister", :ja "大臣、公使"}
               {:en "minute", :ja "分、微細な、微小な"}
               {:en "administer", :ja "治める、施行する、管理する"}
               {:en "mince", :ja "細かく刻む"}]}
   ; 39, 40
   {:en "miss / mise / mit" :ja "送る"
    :examples [{:en "dismiss", :ja "解散させる、退ける"}
               {:en "promise", :ja "約束する"}
               {:en "compromise", :ja "妥協"}
               {:en "admit", :ja "認める、(入場、入学を)認める"}
               {:en "omit", :ja "省略する、除外する、怠る"}
               {:en "permit", :ja "許可する"}
               {:en "commit", :ja "ゆだねる、犯す"}
               {:en "remit", :ja "送金する、差し戻す"}
               {:en "intermittent", :ja "断続的な"}
               {:en "submit", :ja "提出する、屈服する、服従させる"}
               {:en "transmit", :ja "伝える、移す、感染させる"}
               {:en "emit", :ja "発する、放出する"}]}
   ; 41
   {:en "mode" :ja "型、尺度"
    :examples [{:en "remodel", :ja "建て替える、形を直す"}
               {:en "modest", :ja "控えめな、謙虚な"}
               {:en "accommodate", :ja "適応させる、収容する"}
               {:en "mold", :ja "型に入れて作る、形成する"}
               {:en "moderate", :ja "適度の、節度のある"}
               {:en "modify", :ja "修正する、変更する"}]}
   ; 42
   {:en "mot / mob / mov" :ja "動く"
    :examples [{:en "emotion", :ja "感情、情緒"}
               {:en "promote", :ja "昇進させる、促進する"}
               {:en "demote", :ja "降格させる"}
               {:en "motive", :ja "動機、目的"}
               {:en "remote", :ja "遠い、へんぴな"}
               {:en "remove", :ja "取り除く、脱ぐ、解雇する"}]}
   ; 43
   {:en "na(n)t / nai" :ja "生まれる"
    :examples [{:en "pregnant", :ja "妊娠している"}
               {:en "innate", :ja "生まれつきの、固有の"}
               {:en "naturalize", :ja "帰化させる、順応させる"}
               {:en "nationalize", :ja "国有化する"}
               {:en "denationalize", :ja "民営化する"}
               {:en "natal", :ja "出生の、出産の"}]}
   ; 44
   {:en "nom(in) / onym / noun" :ja "名前、伝える"
    :examples [{:en "pronounce", :ja "発音する"}
               {:en "denounce", :ja "告発する、(公然と)非難する"}
               {:en "anonymous", :ja "匿名の、名の分からない"}
               {:en "nominate", :ja "指名する、任命する"}
               {:en "renounce", :ja "拒否する、断念する"}
               {:en "renowned", :ja "有名な"}]}
   ; 45
   {:en "ord(er)" :ja "命令、順序"
    :examples [{:en "disorder", :ja "無秩序、混乱、障害"}
               {:en "ordinary", :ja "普通の、平凡な"}
               {:en "extraordinary", :ja "並外れた、驚くべき、異常な"}
               {:en "orderly", :ja "整然とした、整理された"}
               {:en "subordinate", :ja "下に置く、副次的な、従属する"}
               {:en "coordinate", :ja "対等にする、調整する"}]}
   ; 46
   {:en "part" :ja "分ける、部分"
    :examples [{:en "partial", :ja "部分的な、えこひいきする、とても好きな"}
               {:en "impartial", :ja "公平な、偏らない"}
               {:en "depart", :ja "出発する"}
               {:en "partake", :ja "参加する、(飲食を)共にする"}
               {:en "particular", :ja "特別な"}
               {:en "impart", :ja "与える、添える"}]}
   ; 47
   {:en "pass / pace" :ja "歩、通り過ぎる"
    :examples [{:en "pastime", :ja "娯楽、気晴らし"}
               {:en "surpass", :ja "勝る、越える、上回る"}
               {:en "trespass", :ja "不法侵入する、侵害する"}
               {:en "passenger", :ja "乗客、旅客"}
               {:en "passable", :ja "通行できる、まずまずの、かなり良い"}
               {:en "passage", :ja "通路、道路、通行(権)、一節"}]}
   ; 48
   {:en "path / pass" :ja "感じる、苦しむ"
    :examples [{:en "antipathy", :ja "反感、嫌悪"}
               {:en "anthetic", :ja "無気力の、無感動の、無関心な"}
               {:en "patient", :ja "患者、我慢強い"}
               {:en "sympathize", :ja "同情する、共感する"}
               {:en "passive", :ja "受動的な、消極的な"}
               {:en "compatible", :ja "気が合う、矛盾がない、互換性がある"}]}
   ; 49
   {:en "ped(e) / pod / pus" :ja "足"
    :examples [{:en "peddle", :ja "行商する、売り歩く"}
               {:en "expedition", :ja "探検"}
               {:en "impede", :ja "妨げる"}
               {:en "pedestrian", :ja "歩行者"}
               {:en "expedite", :ja "促進する、手早く片づける"}
               {:en "pedigree", :ja "系図、家計、血統"}]}
   ; 50
   {:en "pel / puls" :ja "押す、打つ"
    :examples [{:en "compel", :ja "強いる、余儀なくさせる"}
               {:en "impulse", :ja "衝撃、衝動"}
               {:en "repulse", :ja "撃退する"}
               {:en "expel", :ja "追放する、除名する"}
               {:en "repel", :ja "寄せ付けない、拒絶する"}
               {:en "dispel", :ja "置い散らす、鳴らす、一掃する"}]}
   ; 51
   {:en "pend" :ja "垂れる、さげる"
    :examples [{:en "depend", :ja "頼る、依存する"}
               {:en "independent", :ja "独立した"}
               {:en "suspend", :ja "一時停止にする、停学(休職、停職)処分にする"}
               {:en "suspense", :ja "緊張感、サスペンス"}
               {:en "impending", :ja "今にも起こりそうな"}
               {:en "perpendicular", :ja "垂直の、切り立った"}]}
   ; 52
   {:en "pend / pens" :ja "(吊るして)計る、支払う"
    :examples [{:en "dispence", :ja "分配する、…なしですます"}
               {:en "expenditure", :ja "支出、経費"}
               {:en "compensate", :ja "償う、埋め合わせる、補償する"}
               {:en "pension", :ja "年金"}
               {:en "pensive", :ja "物思いに沈んだ、悲しげな"}
               {:en "recompense", :ja "弁償をする、報いる"}]}
   ; 53
   {:en "ple / pli / ply" :ja "(折り)重なる、(折り)重ねる"
    :examples [{:en "complicated", :ja "複雑な、難しい"}
               {:en "duplicate", :ja "複写する、真似する"}
               {:en "reply", :ja "返事をする、答える"}
               {:en "explicit", :ja "明白な、わかりやすい"}
               {:en "implicit", :ja "暗黙の、それとなしの、絶対的な"}
               {:en "imply", :ja "暗に意味する、ほのめかす"}]}
   ; 54
   {:en "popul / public / dem" :ja "人、人々"
    :examples [{:en "democracy", :ja "民主主義、民主政治"}
               {:en "publish", :ja "発表する、出版する"}
               {:en "epidemic", :ja "伝染性の、流行する"}
               {:en "population", :ja "人口"}
               {:en "endemic", :ja "(一地方)特有の、固有の"}
               {:en "pandemic", :ja "広域的な、全国的な"}]}
   ; 55
   {:en "port" :ja "運ぶ、港"
    :examples [{:en "export", :ja "輸出する"}
               {:en "import", :ja "輸入する"}
               {:en "opportune", :ja "適切な、好都合な"}
               {:en "support", :ja "支える、養う"}
               {:en "transport", :ja "輸送する"}
               {:en "deport", :ja "国外に追放する"}]}
   ; 56, 57
   {:en "pose / posit" :ja "置く、止まる"
    :examples [{:en "purpose", :ja "目的、意図"}
               {:en "propose", :ja "提案する、(結婚を)申し込む"}
               {:en "impose", :ja "課す、科す、押しつける"}
               {:en "expose", :ja "さらす"}
               {:en "repose", :ja "休憩する、載っている"}
               {:en "suppose", :ja "思う、想像する、もし〜するとしたら"}
               {:en "compose", :ja "作曲する、構成する、作詞する"}
               {:en "dispose", :ja "処分する(of)、始末する、したい気にさせる"}
               {:en "depose", :ja "避ける、免職する"}
               {:en "oppose", :ja "反対する"}
               {:en "composure", :ja "落ち付き、平静"}
               {:en "decompose", :ja "分解する(させる)、腐敗する(させる)"}]}
   ; 58
   {:en "posit / pone" :ja "置く"
    :examples [{:en "postpone", :ja "延期する"}
               {:en "opponent", :ja "敵、相手"}
               {:en "proponent", :ja "支持者、提案者"}
               {:en "positive", :ja "明確な、確信した、積極的な、陽性の"}
               {:en "opposite", :ja "反対側の、正反対の"}
               {:en "deposit", :ja "置く、預金する"}]}
   ; 59
   {:en "press" :ja "押す"
    :examples [{:en "depress", :ja "低迷させる、落胆させる、弱める"}
               {:en "express", :ja "表現する"}
               {:en "impress", :ja "印象を与える、押しつける"}
               {:en "pressing", :ja "差し迫った、緊急な"}
               {:en "oppress", :ja "圧迫する、虐げる、重圧を感じる"}
               {:en "suppress", :ja "鎮圧する、抑える"}]}
   ; 60
   {:en "pri(n) / pri(m)" :ja "一番目の、一つの"
    :examples [{:en "premier", :ja "首相、総理大臣"}
               {:en "principal", :ja "主要な、もっとも重要な"}
               {:en "primitive", :ja "原始(時代)の、原始的な"}
               {:en "principle", :ja "原理、主義"}
               {:en "primary", :ja "第一の、もっとも重要な、最初の"}
               {:en "prime", :ja "第一の、もっとも重要な"}]}
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
      filename (atom nil)
      appeared? (atom #{})
      get-next-root (fn [coll]
                      (let [root (rand-nth coll)]
                        (if (contains? @appeared? root)
                          (recur coll)
                          (do
                            (swap! appeared? conj root)
                            root))))]
  (defn set-filename! [date]
    (let [year (time/year date)
          month (->> date
                     (time/month)
                     (format "%02d"))
          day (->> date
                   (time/day)
                   (format "%02d"))
          file (str "logs/" year "-" month "-" day ".csv")]
      (spit file "")
      (reset! filename file)))
  (defn print-checklist [max-size coll]
    (println *column-size*)
    (println "\\hline")
    (doseq [idx (range 1 (inc max-size))]
      (let [root (get-next-root coll)
            word (rand-nth (:examples root))]
        (spit @filename (str (:en word) ",+\n") :append true)
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

(defn filter-by-mistaken-word-list [mistaken-word-list words]
  (->> words
       (map
        (fn [m]
          (reduce
           (fn [result example]
             (conj result (assoc m :examples [example])))
           [] (get m :examples))))
       (reduce into [])
       (filter
        (fn [w] (contains? mistaken-word-list (-> w :examples first :en))))
       (vec)))

(defn- get-cli-opts [args]
  (cli/cli args
           ["-h" "--help" "Show help" :default false :flag true]
           ["--whole-list" :default false :flag true]
           ["--use-mistaken-word-list" :default false :flag true]
           ["--date" :default (time/today)
            :parse-fn #(parse (formatter "yyyy-MM-dd") %)]))

(defn -main [& args]
  (let [[options rest-args banner] (get-cli-opts args)]
    (when (:help options)
      (println banner)
      (System/exit 0))
    (when (:whole-list options)
      (binding [*out* (java.io.FileWriter. "prefix.tex")]
        (print-tex prefix))
      (binding [*out* (java.io.FileWriter. "root.tex")
                *column-size* "\\begin{longtable}{|p{9em}|p{6em}|p{5em}|p{7em}|}"]
        (print-tex root))
      (binding [*out* (java.io.FileWriter. "suffix.tex")
                *column-size* "\\begin{longtable}{|p{9em}|p{8em}|p{5em}|p{5em}|}"]
        (print-tex suffix))
      (println (sh "omake"))
      (System/exit 0))
    (let [date (:date options)
          year (time/year date)
          month (->> date
                     (time/month)
                     (format "%02d"))
          day (->> date
                   (time/day)
                   (format "%02d"))
          seed (java.util.Random. (hash date))
          root' (if (:use-mistaken-word-list options)
                  (let [mistaken-word-list (set (vec (line-seq (java.io.BufferedReader. *in*))))]
                    (filter-by-mistaken-word-list mistaken-word-list root))
                  root)]
      (set-filename! date)
      (with-redefs [rand (fn [n] (* n (. seed nextDouble)))]
        (binding [*out* (java.io.FileWriter. "checklist_body.tex")]
          (binding [*column-size* "\\begin{table}[!h]
\\begin{tabular}{|p{7em}|p{6em}|p{6em}|p{2em}|p{5em}|p{7em}|}"]
            (print-checklist 35 root')
            (print-checklist 15 root'))
          (println (str year "-" month "-" day ".pdf"))
          (binding [*column-size* "\\begin{table}[!h]
\\begin{tabular}{|p{6em}p{6em}p{6em}p{2em}p{5em}p{8em}|}"]
            (print-empty-table 18))))
      (println (sh "omake"))
      (sh "cp" "checklist.pdf" (str year "-" month "-" day ".pdf"))))
  (shutdown-agents))
