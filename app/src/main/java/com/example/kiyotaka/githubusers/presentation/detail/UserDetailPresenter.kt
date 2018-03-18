package com.example.kiyotaka.githubusers.presentation.detail

import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import io.reactivex.rxkotlin.subscribeBy

/**
 * ユーザー詳細画面のPresenter
 * Created by kiyotaka on 2018/03/18.
 */
class UserDetailPresenter(private val userDetailView: UserDetailView,
                          private val useCase: GitHubUsersUseCase = GitHubUsersUseCase()) {

    fun onCreate(loginId: String) {
        useCase.user(loginId).subscribeBy(
                onNext = { user ->
                    userDetailView.showUser(user)
                })
    }

}