package com.example.kiyotaka.githubusers.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

/**
 * GitHubのユーザーの詳細(ユーザー別APIのレスポンス)
 * Created by kiyotaka on 2018/03/18.
 */
@JsonSerializable
data class GitHubUserDetail(val id: Int,
                            val login: String,
                            @Json(name = "avatar_url")
                            val avatarUrl: String,
                            val location: String?,
                            val email: String?,
                            val followers: Int,
                            @Json(name = "public_repos")
                            val publicRepos: Int)