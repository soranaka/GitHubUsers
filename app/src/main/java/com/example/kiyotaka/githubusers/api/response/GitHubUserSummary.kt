package com.example.kiyotaka.githubusers.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

/**
 * GitHubのユーザーの概要(一覧APIのレスポンス)
 * Created by kiyotaka on 2018/03/18.
 */
@JsonSerializable
data class GitHubUserSummary(val id: Int,
                             val login: String,
                             @Json(name = "avatar_url")
                             val avatarUrl: String)