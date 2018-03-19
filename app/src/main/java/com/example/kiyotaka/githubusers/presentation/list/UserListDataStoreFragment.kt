package com.example.kiyotaka.githubusers.presentation.list

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListDataStore

/**
 * GitHubユーザーリストのロード済みデータを格納しておくFragment
 * Created by kiyotaka on 2018/03/20.
 */
class UserListDataStoreFragment : Fragment(), UserListDataStore {

    var users: List<UserItem>? = null

    override fun getUserList() = users

    override fun setUserList(users: List<UserItem>) {
        this.users = users
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}