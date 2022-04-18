package com.android.stellarsdk.api.callback

interface OnResponse<in T> {

    fun onError(error: String)

    fun onSuccess(response: T)
}