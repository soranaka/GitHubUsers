package com.example.kiyotaka.githubusers.presentation.list

import android.support.annotation.StringRes
import android.view.View
import com.example.kiyotaka.githubusers.domain.model.UserItem

/**
 * GitHubのユーザー一覧画面のinterface
 * Created by kiyotaka on 2018/03/20.
 */
interface UserListConstraint {

    /**
     * GitHubのユーザー一覧画面のView
     * Created by kiyotaka on 2018/03/18.
     */
    interface UserListView {
        fun initView()
        fun updateUsers(users: List<UserItem>)
        fun showUserDetail(v: View, userItem: UserItem)
        fun showErrorMessage(@StringRes messageResId: Int)
    }

    /**
     * ユーザー一覧画面のデータストア
     */
    interface UserListDataStore {
        fun getUserList(): List<UserItem>?
        fun setUserList(users: List<UserItem>)
    }
}