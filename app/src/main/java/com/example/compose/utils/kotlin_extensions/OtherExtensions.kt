package com.example.compose.utils.kotlin_extensions

import java.util.concurrent.TimeUnit

fun Long.toTimeFormat(): String {
    (TimeUnit.MILLISECONDS).let {
        val m = it.toMinutes(this)
        val s = it.toSeconds(this) - TimeUnit.MINUTES.toSeconds(it.toMinutes(this))
        return "$m:$s" + if (s < 10) "0" else ""
    }
}

fun Float.compIn(start: Float = 0f, end: Float = 1f) =
    (coerceIn(start, end) - start) / (end - start)

operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)