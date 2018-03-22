package com.example.kiyotaka.githubusers.presentation.list

import android.util.Log
import android.view.View
import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListDataStore
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListView
import com.example.kiyotaka.githubusers.util.HttpErrorUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * ユーザー一覧のPresenter
 * Created by kiyotaka on 2018/03/18.
 */
class UserListPresenter(private val userListView: UserListView,
                        private val userListDataStore: UserListDataStore,
                        private val useCase: GitHubUsersUseCase = GitHubUsersUseCase()) {

    companion object {
        private const val TAG = "UserListPresenter"
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onCreate() {
        Log.i(TAG, "onCreate")
        userListView.initView()
        val storedUsers = userListDataStore.getUserList()?.also {
            Log.i(TAG, "restore from storedUsers")
        }
        if (storedUsers != null) {
            userListView.updateUsers(storedUsers)
        } else {
            loadUser("0")
        }
    }

    fun onLoadMore(lastUserItem: UserItem) {
        loadUser(lastUserItem.id.toString())
    }

    fun onClickUserItem(v: View, userItem: UserItem) {
        userListView.showUserDetail(v, userItem)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun loadUser(id: String) {
        compositeDisposable.add(useCase.users(id).subscribeBy(
                onNext = { users ->
                    Log.i(TAG, "onNext:$id")
                    val storedUsers = userListDataStore.getUserList()?.toMutableList()
                            ?: mutableListOf()
                    storedUsers.addAll(users)
                    userListDataStore.setUserList(storedUsers)
                    userListView.updateUsers(storedUsers)
                },
                onComplete = {
                    Log.i(TAG, "onComplete:$id")
                },
                onError = { throwable ->
                    Log.w(TAG, "onError:$id", throwable)
                    userListView.showErrorMessage(HttpErrorUtil.convertErrorMessageRes(throwable))
                }))
    }
}