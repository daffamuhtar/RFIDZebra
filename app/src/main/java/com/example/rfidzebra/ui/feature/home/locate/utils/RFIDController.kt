//package com.example.rfidzebra.ui.feature.home.locate.utils
//
//import com.zebra.rfid.api3.*
//
//class RFIDController {
//
//    companion object {
//
//        var asciiMode = false
//        var isLocatingTag = false
//        var currentLocatingTag: String? = null
//        var TagProximityPercent: Short = -1
//
//        private var instance: RFIDController? = null
//        private val mutex = Any()
//
//        @JvmStatic
//        fun getInstance(): RFIDController {
//            var result = instance
//            if (result == null) {
//                synchronized(mutex) {
//                    result = instance
//                    if (result == null) {
//                        instance = RFIDController()
//                        result = instance
//                    }
//                }
//            }
//            return result!!
//        }
//
//    }
//
//    private val inventoryController = InventoryController()
//
//    fun updateTagIDs() {
//        inventoryController.updateTagIDs()
//    }
//
//}
//
