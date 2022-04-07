# ElasticsearchのHigh Level Rest Clientのサンプル

新しいJava Clientがリリースされたので、それとの比較のために古いClientでElasticsearchに対してインデックスしたり検索したりするサンプルを書きました。

## 目的

比較のための実装です。High Level Rest Clientの処理のすべてのサンプルなどではありません。
例外処理などは簡易のものになっています。
一応動くものとなっていますが、外部にElasticsearchサーバーが必要となります。
Wikipediaのデータを数件だけ利用したサンプルになっています
（マッピングについてはCrateTemplateRequestに記述があります）。

## サンプル

* indexer：index templateを作成して、bulkでデータ登録、aliasの切り替え処理などのサンプル
  1. インデックステンプレートの登録
  2. 新インデックスが存在していないことの確認
  3. Bulkでデータ登録
  4. 新旧2つのインデックスの存在確認
  5. 新旧2つのインデックスの件数取得と比較（比較してログ出すだけ）
  6. Alias切り替え
* search：検索処理のサンプル
  1. 検索処理（現時点では単純なmatchクエリのみ） 

## 事前準備

* AbstractEsServiceの変更
  * ローカル環境向けのIPアドレスとなっているので適宜修正が必要

以下は、それぞれのSampleClient.mainメソッドを動かす前の注意点です。

### indexer

* `wikipedia-old-index`というインデックスが存在していることが前提となる処理を書いています。
  * 空のインデックスでもOKです
* Aliasの切り替えをしていますが、oldにエイリアスが存在していなくてもOKです

### search

* 特になし（インデックス名を指定していないので、変更する予定）

## TODO

* 検索のクエリの種類を増やす
* Aggregationの処理が未実装

## ライセンス

MIT