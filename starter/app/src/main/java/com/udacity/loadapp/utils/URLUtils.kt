package com.udacity.loadapp.utils

import android.util.Patterns
import android.webkit.URLUtil

object URLUtils {

    fun isValidURL(url: String): Boolean {
        return (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) &&
                Patterns.WEB_URL.matcher(url).matches()
    }
}
