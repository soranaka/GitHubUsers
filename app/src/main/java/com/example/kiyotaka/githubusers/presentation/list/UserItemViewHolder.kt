package com.example.kiyotaka.githubusers.presentation.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.kiyotaka.githubusers.R

/**
 * ユーザー概要のViewHolder
 * Created by kiyotaka on 2018/03/18.
 */
class UserItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val cardView: View? = itemView
    val avatar: ImageView? = itemView?.findViewById(R.id.avatar) as? ImageView
    val loginId: TextView? = itemView?.findViewById(R.id.login_id) as? TextView
}