package com.example.kiyotaka.githubusers.presentation.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.kiyotaka.githubusers.domain.model.User
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailConstraint.UserDetailDataStore

/**
 * ユーザー詳細画面のロード済みデータを格納しておくFragment
 * Created by kiyotaka on 2018/03/20.
 */
class UserDetailDataStoreFragment : Fragment(), UserDetailDataStore {

    private var data: User? = null

    override fun getUser() = data

    override fun setUser(user: User) {
        this.data = user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}