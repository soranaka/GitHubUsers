package com.example.kiyotaka.githubusers.presentation.list

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.kiyotaka.githubusers.R
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailActivity
import kotlinx.android.synthetic.main.activity_user_list.*


class UserListActivity : AppCompatActivity(), UserListView {

    private lateinit var userListPresenter: UserListPresenter
    private lateinit var recycleViewAdapter: UserListRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        userListPresenter = UserListPresenter(this)
        recycleViewAdapter = UserListRecycleViewAdapter(View.OnClickListener { v ->
            val userItem = v.tag as? UserItem ?: return@OnClickListener
            userListPresenter.onClickUserItem(v, userItem)
        }).also { it.userItems = listOf() }

        userListPresenter.onCreate()
    }

    override fun initView() {
        val layoutManager = LinearLayoutManager(this)
        user_list_recycler_view.adapter = recycleViewAdapter
        user_list_recycler_view.layoutManager = layoutManager
        user_list_recycler_view.addItemDecoration(CustomItemDecoration())
        user_list_recycler_view.addOnScrollListener(
                object : UserListRecycleViewAdapter.EndlessScrollListener(layoutManager) {
                    override fun onLoadMore() {
                        if (recycleViewAdapter.userItems.isEmpty()) {
                            return
                        }
                        val lastUser = recycleViewAdapter.userItems[recycleViewAdapter.userItems.lastIndex]
                        userListPresenter.onLoadMore(lastUser)
                    }
                })

    }

    override fun addUsers(users: List<UserItem>) {
        recycleViewAdapter.userItems = recycleViewAdapter.userItems.toMutableList().also {
            it.addAll(users)
        }
    }

    override fun showUserDetail(v: View, userItem: UserItem) {
        val avatar = v.findViewById(R.id.avatar) as View
        val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, avatar, avatar.transitionName)
        val bundle = compat.toBundle()
        val intent = Intent(this, UserDetailActivity::class.java).also {
            it.putExtra(UserDetailActivity.USER_LOGIN_ID_KEY, userItem.loginId)
            it.putExtra(UserDetailActivity.USER_AVATAR_URL_KEY, userItem.avatarUrl)
        }
        startActivity(intent, bundle)
    }

    class CustomItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            outRect?.top = 5
            outRect?.bottom = 5
        }
    }
}
