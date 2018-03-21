package com.example.kiyotaka.githubusers.domain.model

import com.example.kiyotaka.githubusers.presentation.list.UserListRecycleViewAdapter
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

/**
 * ユーザー一蘭を表示する時のドメインモデル
 * Created by kiyotaka on 2018/03/18.
 */
@JsonSerializable
data class UserItem(
        /**
         * GitHubの内部ID(表示しない)
         */
        val id: Int,
        /**
         * ログイン名
         */
        @Json(name = "login_id")
        val loginId: String,
        /**
         * アバター画像のURL
         */
        @Json(name = "avatar_url")
        val avatarUrl: String) : UserListRecycleViewAdapter.Diffable