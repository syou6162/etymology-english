# etymology-english

「連想式でみるみる身につく語源で英単語」の接頭辞、接尾辞、語幹を表にまとめたものです。色んな形式で後で遊べるようにclojureでデータ持たせているだけ。

## Usage

`platex`や`omake`の入った状態で`lein run`すると`main.pdf`ができあがります。

```sh
lein run --whole-list
grep -h ",-" logs/*.csv | cut -d "," -f 1 | sort | uniq -c | sort -k 1 -r | cut -d " " -f 5 | lein run --use-mistaken-word-list --date 2014-01-01
```

## Herokuへのdeployの仕方

herokuコマンドでbuildpackのurlを登録。

```sh
heroku config:add BUILDPACK_URL=https://github.com/kolov/heroku-buildpack-clojure
```

project.cljでleinのバージョンの下限を指定。

```clj
  :min-lein-version "2.0.0"

```

Procfileは`lein run`ではなく以下を指定。

```
web: lein with-profile offline,production trampoline run -m etymology-english.json-server
```

## License

Copyright © 2014 Yasuhisa Yoshida

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
