package com.example.kiyotaka.githubusers.presentation.detail

import android.util.Log
import com.example.kiyotaka.githubusers.domain.GitHubUsersUseCase
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailConstraint.UserDetailDataStore
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailConstraint.UserDetailView
import com.example.kiyotaka.githubusers.util.HttpErrorUtil
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * ユーザー詳細画面のPresenter
 * Created by kiyotaka on 2018/03/18.
 */
class UserDetailPresenter(private val userDetailView: UserDetailView,
                          private val UserDetailDataStore: UserDetailDataStore,
                          private val useCase: GitHubUsersUseCase = GitHubUsersUseCase()) {

    companion object {
        private const val TAG = "UserDetailPresenter"
    }

    private var disposable: Disposable? = null

    fun onCreate(loginId: String) {
        val storedUser = UserDetailDataStore.getUser()?.also {
            Log.i(TAG, "restore from storedUsers")
        }
        if (storedUser != null) {
            userDetailView.showUser(storedUser)
            return
        }
        disposable = useCase.user(loginId).subscribeBy(
                onNext = { user ->
                    UserDetailDataStore.setUser(user)
                    userDetailView.showUser(user)
                },
                onError = { throwable ->
                    Log.i(TAG, "onError:$loginId", throwable)
                    userDetailView.showErrorMessage(HttpErrorUtil.convertErrorMessageRes(throwable))
                })
    }

    fun onDestroy() {
        disposable?.let {
            if (!it.isDisposed) it.dispose()
        }
        disposable = null
    }
}