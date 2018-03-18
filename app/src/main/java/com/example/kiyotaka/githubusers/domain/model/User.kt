package com.example.kiyotaka.githubusers.domain.model

/**
 * ユーザーの詳細情報を表示する時のドメインモデル
 * Created by kiyotaka on 2018/03/18.
 */
data class User(
        /**
         * ログイン名
         */
        val loginId: String,
        /**
         * アバター画像のURL
         */
        val avatarUrl: String,
        /**
         * 住んでいるところ
         */
        val location: String?,
        /**
         * メールアドレス
         */
        val email: String?,
        /**
         * 公開リポジトリ数
         */
        val publicRepos: Int,
        /**
         *　フォロワー数
         */
        val followers: Int)