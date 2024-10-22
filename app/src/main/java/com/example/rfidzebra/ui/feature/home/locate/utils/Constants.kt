package com.example.rfidzebra.ui.feature.home.locate.utils

import android.util.Log


object Constants {
    const val IMMEDIATE = "Immediate"
    const val HANDHELD = "Handheld"
    const val PERIODIC = "Periodic"
    const val DURATION = "Duration"
    const val TAG_OBSERVATION = "Tag Observation"
    const val N_ATTEMPTS = "N attempts"
    const val BATCH_MODE_ATTR_NUM = 1500
    const val QUIET_BEEPER = 3
    const val READER_PASSWORDS = "ReadersPasswords"
    const val ZERO_TIME = "00:00"
    const val FROM_NOTIFICATION = "fromNotification"
    const val MESSAGE_BATTERY_CRITICAL = "Battery level critical"
    const val MESSAGE_BATTERY_LOW = "Battery level low"
    const val BATTERY_FULL = 100
    const val BATTERY_LOW = 10
    const val BATTERY_CRITICAL = 5

    //For Debugging
    const val DEBUG = false
    const val TYPE_DEBUG = 60
    const val TYPE_ERROR = 61

    //Intent Data
    const val INTENT_ACTION = "intent_action"
    const val INTENT_DATA = "intent_data"

    //Action strings for various RFID Events
    const val ACTION_READER_BATTERY_LOW = "com.rfidreader.battery.low"
    const val ACTION_READER_BATTERY_CRITICAL = "com.rfidreader.battery.critical"
    const val ACTION_READER_CONNECTED = "com.rfidreader.connected"
    const val ACTION_READER_DISCONNECTED = "com.rfidreader.disconnected"
    const val ACTION_READER_AVAILABLE = "com.rfidreader.available"
    const val ACTION_READER_CONN_FAILED = "com.rfidreader.conn.failed"
    const val ACTION_READER_STATUS_OBTAINED = "com.rfidreader.status.received"
    const val ACTION_READER_SET_REGION = "com.rfidreader.set.region"

    //Data related to notifications
    const val NOTIFICATIONS_TEXT = "notifications_text"
    const val NOTIFICATIONS_ID = "notifications_id"

    //timeout for sled response
    const val RESPONSE_TIMEOUT = 6000
    const val SAVE_CONFIG_RESPONSE_TIMEOUT: Long = 15000

    //Strings for storing the checkbox status of connection settings in shared preferences
    const val APP_SETTINGS_STATUS = "AppSettingStatus"
    const val AUTO_DETECT_READERS = "AutoDetectReaders"
    const val AUTO_RECONNECT_READERS = "AutoReconnectReaders"
    const val NOTIFY_READER_AVAILABLE = "NotifyReaderAvailable"
    const val NOTIFY_READER_CONNECTION = "NotifyReaderConnection"
    const val NOTIFY_BATTERY_STATUS = "NotifyBatteryStatus"
    const val EXPORT_DATA = "ExportData"
    const val TAG_LIST_MATCH_MODE = "TagListMatchMode"
    const val SHOW_CSV_TAG_NAMES = "TagListMatchcsvTagNames"
    const val NON_MATCHING = "PRE_FILTER_NON_MATCHING"
    const val PREFILTER_ADV_OPTIONS = "PREFILTER_ADV_OPTIONS"
    const val ACCESS_ADV_OPTIONS = "ACCESS_ADV_OPTIONS"
    const val ASCII_MODE = "ASCII_MODE"
    const val SGTIN_MODE = "SGTIN_MODE"
    const val LAST_READER = "LAST_CONNECTED_READER"
    const val KEYLAYOUT = "KEYLAYOUT"

    // Canned profile storage
    const val APP_SETTINGS_PROFILE = "AppSettingProfile"
    const val PROFILE_POWER = "PROFILE_POWER"
    const val PROFILE_LINK_PROFILE = "PROFILE_LINK_PROFILE"
    const val PROFILE_SESSION = "PROFILE_SESSION"
    const val PROFILE_DPO = "PROFILE_DPO"
    const val PROFILE_IS_ON = "PROFILE_IS_ON"
    const val PROFILE_UI_ENABLED = "PROFILE_UI_ENABLED"
    const val MULTITAG_LOCATE_DATA_SORT = "MultiTagLocate"
    const val MULTITAG_LOCATE_FOUND_PROXI_PERCENT_DATA = "MultiTagLocateFoundProxiPercent"
    const val MULTITAG_LOCATE_CSV_URI = "MultiTagLocateCsv"

    //Bundle name for setting item selected
    const val SETTING_ITEM_ID = "settingItemId"
    const val SETTING_ON_FACTORY = "settingOnFactory"
    const val UNIQUE_TAG_LIMIT = 120000
    const val ON = "ON"
    const val OFF = "OFF"
    const val NGE = "NGE"
    const val RFID_DEVICE = "RFID_DEVICE"
    const val GENX_DEVICE = "GENX_DEVICE"

    //toast messages
    const val TAG_EMPTY = "Please fill Tag Id"
    const val NO_OF_BITS = 16
    const val TAGS_SEC = "" //" t/s";

    //max offset for prefilter and access
    const val MAX_OFFSET = 1024

    //max offset for access
    const val MAX_LEGTH = 1024

    // tag match file name and directory
    const val RFID_FILE_DIR = "/rfid"
    const val TAG_MATCH_FILE_NAME = "taglist.csv"
    var INVENTORY_LIST_FONT_SIZE = 0
    var showSerialNo = false

    /**
     * Method to be used throughout the app for logging debug messages
     *
     * @param type    - One of TYPE_ERROR or TYPE_DEBUG
     * @param TAG     - Simple String indicating the origin of the message
     * @param message - Message to be logged
     */
    fun logAsMessage(type: Int, tag: String?, message: String?) {
        if (DEBUG) {
            if (type == TYPE_DEBUG) Log.d(
                tag,
                if (message.isNullOrEmpty()) "Message is Empty!!" else message
            ) else if (type == TYPE_ERROR) Log.e(
                tag,
                if (message.isNullOrEmpty()) "Message is Empty!!" else message
            )
        }
    }
}
