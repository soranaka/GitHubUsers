package com.example.kiyotaka.githubusers.presentation.list

import android.util.Log
import android.view.View
import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListDataStore
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListView
import io.reactivex.disposables.Disposable
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

    private var disposable: Disposable? = null

    fun onCreate() {
        userListView.initView()
        val storedUsers = userListDataStore.getUserList()
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
        disposable?.let {
            if (!it.isDisposed) it.dispose()
        }
        disposable = null
    }

    private fun loadUser(id: String) {
        disposable = useCase.users(id).subscribeBy(
                onNext = { users ->
                    val storedUsers = userListDataStore.getUserList()?.toMutableList()
                            ?: mutableListOf()
                    storedUsers.addAll(users)
                    userListDataStore.setUserList(storedUsers)
                    userListView.updateUsers(storedUsers)
                },
                onComplete = {
                    Log.i(TAG, "onComplete:$id")
                })
    }
}