package com.example.rfidzebra.ui.feature.home.locate.utils

import android.app.Application
import com.zebra.scannercontrol.SDKHandler

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        sdkHandler = SDKHandler(this, false)
    }

    companion object {

        var tagIDs: ArrayList<String>? = null
        var locateTag: String? = null
        var sdkHandler: SDKHandler? = null

    }
}
