package com.example.rfidzebra.ui.feature.home.locate.utils

import com.zebra.rfid.api3.RFIDResults
import com.zebra.rfid.api3.ReaderDevice

class ResponseHandlerInterfaces {

    interface TriggerEventHandler {

        fun triggerPressEventRecieved()

        fun triggerReleaseEventRecieved()
    }

}