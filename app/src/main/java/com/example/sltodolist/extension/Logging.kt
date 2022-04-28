package com.example.sltodolist.extension

import timber.log.Timber

inline fun Any.logd(
    tag: String? = null,
    messageProvider: () -> String
) {
    Timber.tag(tag ?: this::class.java.simpleName).d(messageProvider())
}