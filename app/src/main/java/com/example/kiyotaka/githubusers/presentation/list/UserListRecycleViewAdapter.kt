package com.example.kiyotaka.githubusers.presentation.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kiyotaka.githubusers.R
import com.example.kiyotaka.githubusers.domain.model.UserItem
import kotlin.properties.Delegates


/**
 * ユーザー一覧のRecycleViewAdapter
 * Created by kiyotaka on 2018/03/18.
 */
class UserListRecycleViewAdapter(private val onClickListener: View.OnClickListener)
    : RecyclerView.Adapter<UserItemViewHolder>() {

    var userItems: List<UserItem> by Delegates.observable(emptyList()) { _, old, new ->
        calculateDiff(old, new).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserItemViewHolder(view)
    }

    override fun getItemCount() = userItems.size

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val context = holder.itemView?.context ?: return
        val imageItem = userItems[position]
        holder.cardView?.tag = imageItem
        holder.cardView?.setOnClickListener(onClickListener)
        holder.loginId?.text = imageItem.loginId
        holder.avatar?.transitionName = imageItem.loginId
        Glide.with(context).load(imageItem.avatarUrl).into(holder.avatar!!)
    }

    private fun calculateDiff(old: List<Diffable>, new: List<Diffable>, detectMoves: Boolean = false)
            : DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(Callback(old, new), detectMoves)
    }

    interface Diffable {
        fun isTheSame(other: Diffable): Boolean = equals(other)
        fun isContentsTheSame(other: Diffable): Boolean = equals(other)
    }

    private class Callback(val old: List<Diffable>, val new: List<Diffable>)
        : DiffUtil.Callback() {

        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].isTheSame(new[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].isContentsTheSame(new[newItemPosition])
        }
    }

    abstract class EndlessScrollListener(private val linearLayoutManager: LinearLayoutManager)
        : RecyclerView.OnScrollListener() {

        // これより少なくなったら次をロードする
        private val visibleThreshold = 15

        private var firstVisibleItem: Int = 0
        private var visibleItemCount: Int = 0
        private var totalItemCount: Int = 0
        private var previousTotal = 0
        private var loading = true

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            visibleItemCount = recyclerView!!.childCount
            totalItemCount = linearLayoutManager.itemCount
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }

            if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                onLoadMore()
                loading = true
            }
        }

        abstract fun onLoadMore()
    }
}