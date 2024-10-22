package com.example.rfidzebra.ui.feature.encapsulation.obj

import com.example.rfidzebra.ui.feature.encapsulation.clasess.HoneywellRFIDSDK
import com.example.rfidzebra.ui.feature.ConnectActivity
import com.example.rfidzebra.ui.feature.RFIDHandlerNew
import com.example.rfidzebra.ui.feature.encapsulation.interf.RFIDSDK

object RFIDSDKFactory {
    fun createSDK(readerType: String, context: ConnectActivity): RFIDSDK {
        return when (readerType) {
            "Zebra" -> RFIDHandlerNew(context)
            "Honeywell" -> HoneywellRFIDSDK(context)
            else -> throw IllegalArgumentException("Unsupported reader type")
        }
    }
}


