package com.example.kiyotaka.githubusers.presentation.detail

import com.example.kiyotaka.githubusers.domain.model.User

/**
 * ユーザーの詳細画面のView
 * Created by kiyotaka on 2018/03/18.
 */
interface UserDetailView {
    fun initView()
    fun showUser(user: User)
}