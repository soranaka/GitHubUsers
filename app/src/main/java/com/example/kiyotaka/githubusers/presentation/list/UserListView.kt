package com.example.kiyotaka.githubusers.presentation.list

import android.view.View
import com.example.kiyotaka.githubusers.domain.model.UserItem

/**
 * GitHubのユーザー一覧画面のView
 * Created by kiyotaka on 2018/03/18.
 */
interface UserListView {
    fun initView()
    fun addUsers(users: List<UserItem>)
    fun showUserDetail(v: View, userItem: UserItem)
}