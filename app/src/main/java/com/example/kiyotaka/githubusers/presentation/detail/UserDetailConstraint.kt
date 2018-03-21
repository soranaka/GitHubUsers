package com.example.kiyotaka.githubusers.presentation.detail

import android.support.annotation.StringRes
import com.example.kiyotaka.githubusers.domain.model.User

/**
 * ユーザーの詳細画面のinterface
 * Created by kiyotaka on 2018/03/20.
 */
interface UserDetailConstraint {

    /**
     * ユーザーの詳細画面のView
     * Created by kiyotaka on 2018/03/18.
     */
    interface UserDetailView {
        fun initView()
        fun showUser(user: User)
        fun showErrorMessage(@StringRes messageResId: Int)
    }

    /**
     * ユーザーの詳細画面のデータストア
     */
    interface UserDetailDataStore {
        fun getUser(): User?
        fun setUser(user: User)
    }
}