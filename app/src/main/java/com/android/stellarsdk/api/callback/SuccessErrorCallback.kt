package com.android.stellarsdk.api.callback

interface SuccessErrorCallback {
    fun onSuccess()
    fun onError(error: String)
}