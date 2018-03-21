package com.example.kiyotaka.githubusers.presentation.list

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListDataStore
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListView
import com.example.kiyotaka.githubusers.util.HttpErrorUtil
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
        private const val SAVE_DATA_KEY = "save_data_key"
        private val ADAPTER = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                .adapter<List<UserItem>>(Types.newParameterizedType(List::class.java, UserItem::class.java))
    }

    private var disposable: Disposable? = null

    fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        userListView.initView()
        val storedUsers = userListDataStore.getUserList()?.also {
            Log.i(TAG, "restore from storedUsers")
        } ?: savedInstanceState?.getString(SAVE_DATA_KEY)?.let { json ->
            ADAPTER.fromJson(json)
        }?.also {
            Log.i(TAG, "restore from savedInstanceState")
            userListDataStore.setUserList(it)
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
        disposable?.let {
            if (!it.isDisposed) it.dispose()
        }
        disposable = null
    }

    fun onSaveInstanceState(outState: Bundle?) {
        val storedUsers = userListDataStore.getUserList() ?: return
        val json = ADAPTER.toJson(storedUsers)
        outState?.putString(SAVE_DATA_KEY, json)
    }

    private fun loadUser(id: String) {
        disposable = useCase.users(id).subscribeBy(
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
                })
    }
}