package com.example.kiyotaka.githubusers.presentation.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kiyotaka.githubusers.R
import com.example.kiyotaka.githubusers.domain.model.User
import com.example.kiyotaka.githubusers.presentation.detail.UserDetailConstraint.UserDetailView
import com.github.florent37.viewanimator.ViewAnimator
import kotlinx.android.synthetic.main.activity_user_detail.*

/**
 * ユーザーの詳細画面のActivity
 * Created by kiyotaka on 2018/03/18.
 */
class UserDetailActivity : AppCompatActivity(), UserDetailView {

    companion object {
        private const val TAG = "UserDetailActivity"
        private const val DATA_STORE_TAG = "data_store_tag"
        const val USER_LOGIN_ID_KEY = "user_login_id_key"
        const val USER_AVATAR_URL_KEY = "user_avatar_url_key"
    }

    private lateinit var presenter: UserDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_user_detail)

        val fm = supportFragmentManager
        val dataStore = fm.findFragmentByTag(DATA_STORE_TAG) as? UserDetailDataStoreFragment
                ?: UserDetailDataStoreFragment().also {
                    fm.beginTransaction().add(it, DATA_STORE_TAG).commit()
                }

        presenter = UserDetailPresenter(this, dataStore)

        val loginId = intent?.getStringExtra(USER_LOGIN_ID_KEY)
        val avatarUrl = intent?.getStringExtra(USER_AVATAR_URL_KEY)

        // アバター画像を読み込んだ後にSharedElementでアニメーションさせるために一旦止める
        supportPostponeEnterTransition()
        avatar.transitionName = loginId
        // Glideに読み込み済みのURLを渡すことでキャッシュを利用させる(消えてなければ利用できる)
        Glide.with(this)
                .load(avatarUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(avatar)
        presenter.onCreate(loginId!!)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun initView() {
    }

    override fun showUser(user: User) {

        login_id.text = user.loginId
        location.text = user.location
        email.text = user.email
        public_repos.text = user.publicRepos.toString()
        followers.text = user.followers.toString()

        // フェードインさせる
        ViewAnimator.animate(login_id_title, login_id, location_title, location, email_title,
                email_title, email, public_repos_title, public_repos, followers_title, followers)
                .onStart {
                    login_id_title.visibility = View.VISIBLE
                    login_id.visibility = View.VISIBLE
                    location_title.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    email_title.visibility = View.VISIBLE
                    email.visibility = View.VISIBLE
                    public_repos_title.visibility = View.VISIBLE
                    public_repos.visibility = View.VISIBLE
                    followers_title.visibility = View.VISIBLE
                    followers.visibility = View.VISIBLE
                }
                .fadeIn()
                .duration(200)
                .start()
    }

    override fun showErrorMessage(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
    }
}