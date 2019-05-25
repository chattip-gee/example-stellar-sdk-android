package com.android.stellarsdk

import android.app.Application
import com.android.stellarsdk.api.remote.Horizon
import com.android.stellarsdk.api.remote.ServerType
import com.android.stellarsdk.api.restapi.ApiConstant

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Horizon.init(ServerType.TEST_NET)
        ApiConstant.init("https://www.stellar.org/laboratory/")
    }
}