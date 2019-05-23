package com.android.stellarsdk

import android.app.Application
import com.android.stellarsdk.api.ApiConstant

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiConstant.init("https://www.stellar.org/laboratory/")
    }
}