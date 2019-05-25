package com.android.stellarsdk.api.callback

interface OnResponse<in T> {

    fun onApiError(error: String)

    fun onApiSuccess(response: T)
}