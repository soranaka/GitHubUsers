package com.example.kiyotaka.githubusers.api

import com.example.kiyotaka.githubusers.api.response.GitHubUserDetail
import com.example.kiyotaka.githubusers.api.response.GitHubUserSummary
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * GitHubのユーザーAPI
 * Created by kiyotaka on 2018/03/18.
 */
interface GitHubUserApi {
    @GET("/users")
    fun users(@Query("since", encoded = true) since: String)
            : Observable<List<GitHubUserSummary>>

    @GET("/users/{username}")
    fun user(@Path("username", encoded = true) username: String)
            : Observable<GitHubUserDetail>
}