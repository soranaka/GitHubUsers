package com.example.kiyotaka.githubusers.domain.model

import com.example.kiyotaka.githubusers.presentation.list.UserListRecycleViewAdapter

/**
 * ユーザー一蘭を表示する時のドメインモデル
 * Created by kiyotaka on 2018/03/18.
 */
data class UserItem(
        /**
         * GitHubの内部ID(表示しない)
         */
        val id: Int,
        /**
         * ログイン名
         */
        val loginId: String,
        /**
         * アバター画像のURL
         */
        val avatarUrl: String) : UserListRecycleViewAdapter.Diffable