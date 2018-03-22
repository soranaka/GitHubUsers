package com.example.kiyotaka.githubusers.presentation.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListDataStore
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * GitHubユーザーリストのロード済みデータを格納しておくFragment
 * Created by kiyotaka on 2018/03/20.
 */
class UserListDataStoreFragment : Fragment(), UserListDataStore {
    companion object {
        private const val TAG = "UserListDataStore"
        private const val SAVE_DATA_KEY = "save_data_key"
        private val ADAPTER = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                .adapter<List<UserItem>>(Types.newParameterizedType(List::class.java, UserItem::class.java))
    }

    private var users: List<UserItem>? = null

    override fun getUserList() = users

    override fun setUserList(users: List<UserItem>) {
        this.users = users
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        savedInstanceState?.getString(SAVE_DATA_KEY)?.let { json ->
            ADAPTER.fromJson(json)
        }?.also {
            Log.i(TAG, "restore from savedInstanceState")
            setUserList(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val storedUsers = users ?: return
        val json = ADAPTER.toJson(storedUsers)
        outState.putString(SAVE_DATA_KEY, json)
    }
}