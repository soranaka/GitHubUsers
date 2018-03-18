package com.example.kiyotaka.githubusers.presentation.list

import android.util.Log
import android.view.View
import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import com.example.kiyotaka.githubusers.domain.model.UserItem
import io.reactivex.rxkotlin.subscribeBy

/**
 * ユーザー一覧のPresenter
 * Created by kiyotaka on 2018/03/18.
 */
class UserListPresenter(private val userListView: UserListView,
                        private val useCase: GitHubUsersUseCase = GitHubUsersUseCase()) {

    companion object {
        private const val TAG = "UserListPresenter"
    }

    fun onCreate() {
        userListView.initView()
        useCase.users().subscribeBy(
                onNext = { users ->
                    userListView.addUsers(users)
                }
        )
    }

    fun onLoadMore(lastUserItem: UserItem) {
        useCase.users(lastUserItem.id.toString()).subscribeBy(
                onNext = { users ->
                    userListView.addUsers(users)
                },
                onComplete = {
                    Log.i(TAG, "onComplete:${lastUserItem.id}")
                }
        )
    }

    fun onClickUserItem(v: View, userItem: UserItem) {
        userListView.showUserDetail(v, userItem)
    }
}