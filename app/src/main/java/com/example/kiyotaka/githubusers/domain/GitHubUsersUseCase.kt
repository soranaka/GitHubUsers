package com.example.kiyotaka.githubusers.domain

import com.example.kiyotaka.githubusers.api.GitHubUserApi
import com.example.kiyotaka.githubusers.domain.model.User
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * GitHubのユーザー閲覧のusecase
 * Created by kiyotaka on 2018/03/18.
 */
class GitHubUsersUseCase {
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun users(since: String = "0"): Observable<List<UserItem>> {
        return retrofit.create(GitHubUserApi::class.java).users(since)
                .map { response ->
                    response.map { summary ->
                        UserItem(id = summary.id,
                                loginId = summary.login,
                                avatarUrl = summary.avatarUrl)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun user(loginId: String): Observable<User> {
        return retrofit.create(GitHubUserApi::class.java).user(loginId)
                .map { detail ->
                    User(loginId = detail.login,
                            avatarUrl = detail.avatarUrl,
                            location = detail.location,
                            email = detail.email,
                            publicRepos = detail.publicRepos,
                            followers = detail.followers)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}