package com.example.compose.utils.kotlin_extensions

import java.util.concurrent.TimeUnit

fun Long.toTimeFormat(): String {
    (TimeUnit.MILLISECONDS).let {
        val m = it.toMinutes(this)
        val s = it.toSeconds(this) - TimeUnit.MINUTES.toSeconds(it.toMinutes(this))
        return "$m:$s" + if (s<10) "0" else ""
    }
}