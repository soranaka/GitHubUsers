package com.example.kiyotaka.githubusers.util

import android.support.annotation.StringRes
import com.example.kiyotaka.githubusers.R
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * 通信エラーUtil
 * Created by kiyotaka on 2018/03/21.
 */
object HttpErrorUtil {
    @StringRes
    fun convertErrorMessageRes(throwable: Throwable): Int {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        R.string.error_message_forbidden
                    }
                    else -> R.string.error_message_unknown
                }
            }
            else -> {
                R.string.error_message_unknown
            }
        }
    }
}