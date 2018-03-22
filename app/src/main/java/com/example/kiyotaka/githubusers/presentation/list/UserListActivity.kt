package com.example.kiyotaka.githubusers.presentation.list

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.kiyotaka.githubusers.R
import com.example.kiyotaka.githubusers.domain.model.UserItem
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailActivity
import com.example.kiyotaka.githubusers.presentation.list.UserListConstraint.UserListView
import kotlinx.android.synthetic.main.activity_user_list.*


class UserListActivity : AppCompatActivity(), UserListView {

    companion object {
        private const val DATA_STORE_TAG = "data_store_tag"
    }

    private lateinit var userListPresenter: UserListPresenter
    private lateinit var recycleViewAdapter: UserListRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val fm = supportFragmentManager
        val dataStore = fm.findFragmentByTag(DATA_STORE_TAG) as? UserListDataStoreFragment
                ?: UserListDataStoreFragment().also {
                    fm.beginTransaction().add(it, DATA_STORE_TAG).commit()
                }
        userListPresenter = UserListPresenter(this, dataStore)
        recycleViewAdapter = UserListRecycleViewAdapter(View.OnClickListener { v ->
            val userItem = v.tag as? UserItem ?: return@OnClickListener
            userListPresenter.onClickUserItem(v, userItem)
        }).also { it.userItems = listOf() }

        userListPresenter.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        userListPresenter.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        userListPresenter.onDestroy()
        super.onDestroy()
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

    override fun updateUsers(users: List<UserItem>) {
        recycleViewAdapter.userItems = users
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

    override fun showErrorMessage(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
    }

    class CustomItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            view ?: return
            parent ?: return
            val userCardSpace = view.context.resources.getDimensionPixelSize(R.dimen.user_card_space)
            val position = parent.getChildAdapterPosition(view)

            outRect?.also {
                // 先頭だけtopマージンを設定する
                if (position == 0) {
                    it.top = userCardSpace
                }
                it.left = userCardSpace
                it.right = userCardSpace
                it.bottom = userCardSpace
            }
        }
    }
}
