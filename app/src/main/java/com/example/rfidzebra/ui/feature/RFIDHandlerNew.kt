package com.example.rfidzebra.ui.feature

import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.example.rfidzebra.ui.feature.encapsulation.interf.RFIDSDK
import com.zebra.rfid.api3.ENUM_TRANSPORT
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.InvalidUsageException
import com.zebra.rfid.api3.OperationFailureException
import com.zebra.rfid.api3.RFIDReader
import com.zebra.rfid.api3.ReaderDevice
import com.zebra.rfid.api3.Readers
import com.zebra.rfid.api3.Readers.RFIDReaderEventHandler
import com.zebra.rfid.api3.RfidEventsListener
import com.zebra.rfid.api3.RfidReadEvents
import com.zebra.rfid.api3.RfidStatusEvents
import com.zebra.rfid.api3.SESSION
import com.zebra.rfid.api3.SL_FLAG
import com.zebra.rfid.api3.START_TRIGGER_TYPE
import com.zebra.rfid.api3.STATUS_EVENT_TYPE
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE
import com.zebra.rfid.api3.TagData
import com.zebra.rfid.api3.TriggerInfo
import com.zebra.scannercontrol.DCSSDKDefs
import com.zebra.scannercontrol.DCSScannerInfo
import com.zebra.scannercontrol.FirmwareUpdateEvent
import com.zebra.scannercontrol.IDcsSdkApiDelegate
import com.zebra.scannercontrol.SDKHandler

class RFIDHandlerNew(
    private var context: ConnectActivity
) : RFIDSDK, IDcsSdkApiDelegate, RFIDReaderEventHandler {

    // View Binding Instance
    private val binding by lazy { context.binding }

    private var readers: Readers? = null

    // Inisialisasi SDK dan mencari pembaca yang tersedia
    private fun initSDK() {
        Log.d(TAG, "initSDK")
        if (readers == null) {
            CreateInstanceTask().execute()
        }
    }

    private var availableRFIDReaderList: ArrayList<ReaderDevice>? = null
    private var readerDevice: ReaderDevice? = null
    private var reader: RFIDReader? = null
    private var eventHandler: EventHandler? = null
    private var sdkHandler: SDKHandler? = null
    private var scannerList: ArrayList<DCSScannerInfo>? = null
    private var scannerID = 0
    private var maxPower = 270

    private var readerName = "RFD4031-G10B700-US"

    fun onCreate() {
        scannerList = ArrayList()
        initSDK()
    }

    override fun dcssdkEventScannerAppeared(dcsScannerInfo: DCSScannerInfo) {}
    override fun dcssdkEventScannerDisappeared(i: Int) {}
    override fun dcssdkEventCommunicationSessionEstablished(dcsScannerInfo: DCSScannerInfo) {}
    override fun dcssdkEventCommunicationSessionTerminated(i: Int) {}
    override fun dcssdkEventBarcode(barcodeData: ByteArray, barcodeType: Int, fromScannerID: Int) {
        val s = String(barcodeData)
        context.barcodeData(s)
        Log.d(TAG, "barcode =$s")
    }

    override fun dcssdkEventImage(bytes: ByteArray, i: Int) {}
    override fun dcssdkEventVideo(bytes: ByteArray, i: Int) {}
    override fun dcssdkEventBinaryData(bytes: ByteArray, i: Int) {}
    override fun dcssdkEventFirmwareUpdate(firmwareUpdateEvent: FirmwareUpdateEvent) {}
    override fun dcssdkEventAuxScannerAppeared(
        dcsScannerInfo: DCSScannerInfo,
        dcsScannerInfo1: DCSScannerInfo
    ) {
    }

    private val isReaderConnected: Boolean
        get() = if (reader != null && reader!!.isConnected) true else {
            Log.d(TAG, "reader is not connected")
            false
        }

    fun onDestroy() {
        dispose()
    }

    // Enumerates SDK based on host device
    private inner class CreateInstanceTask :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg voids: Void?): Void? {
            Log.d(TAG, "CreateInstanceTask")
            // Based on support available on host device choose the reader type
            var invalidUsageException: InvalidUsageException? = null
            readers = Readers(context, ENUM_TRANSPORT.SERVICE_USB)
            try {
                availableRFIDReaderList = readers!!.GetAvailableRFIDReaderList()
            } catch (e: InvalidUsageException) {
                invalidUsageException = e
                e.printStackTrace()
            }
            if (invalidUsageException != null || availableRFIDReaderList!!.size == 0) {
                readers!!.Dispose()
                readers = null
                if (readers == null) {
                    readers = Readers(context, ENUM_TRANSPORT.BLUETOOTH)
                }
            }
            return null
        }

    }

    @Synchronized
    override fun connectReader(): String {
        return if (!isReaderConnected) {
            binding.progressBar.visibility = View.VISIBLE
            ConnectionTask().execute()
            "Connecting..."
        } else {
            "Already Connected"
        }
    }

    override fun disconnectReader(): String {
        TODO("Not yet implemented")
    }

    private inner class ConnectionTask :
        AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            Log.d(TAG, "ConnectionTask")
            getAvailableReader()
            return if (reader != null) connect() else "Failed to find or connect reader"
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            binding.progressBar.visibility = View.GONE
            binding.textConnect.text = result
        }

    }


    @Synchronized
    private fun getAvailableReader() {
        Log.d(TAG, "getAvailableReader")
        if (readers != null) {
            Readers.attach(this)
            try {
                val availableReaders = readers!!.GetAvailableRFIDReaderList()
                if (availableReaders != null) {
                    availableRFIDReaderList = availableReaders
                    Log.d("getAvailableReader", "Available RFID Readers: ${availableRFIDReaderList!!.size}")
                    if (availableRFIDReaderList!!.size != 0) {
                        for (device in availableRFIDReaderList!!) {
                            Log.d("getAvailableReader", "Device Name: ${device.name}")
                            Log.d("getAvailableReader", "Device Info: $device")
                        }
                        // if single reader is available then connect it
                        if (availableRFIDReaderList!!.size == 1) {
                            readerDevice = availableRFIDReaderList!![0]
                            reader = readerDevice!!.rfidReader
                        } else {
                            // search reader specified by name
                            for (device in availableRFIDReaderList!!) {
                                if (device.name.startsWith(readerName)) {
                                    readerDevice = device
                                    reader = readerDevice!!.rfidReader
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "No RFID Readers available.")
                    }
                } else {
                    Log.d(TAG, "No available RFID Readers were returned.")
                }
            } catch (ie: InvalidUsageException) {
                ie.printStackTrace()
            }
        }
    }

    // handler for receiving reader appearance events
    override fun RFIDReaderAppeared(readerDevice: ReaderDevice) {
        Log.d(TAG, "RFIDReaderAppeared " + readerDevice.name)
        context.sendToast("RFIDReaderAppeared")
//        connectReader()
    }

    override fun RFIDReaderDisappeared(readerDevice: ReaderDevice) {
        Log.d(TAG, "RFIDReaderDisappeared " + readerDevice.name)
        context.sendToast("RFIDReaderDisappeared")
        if (readerDevice.name == reader!!.hostName) disconnect()
    }

    @Synchronized
    private fun connect(): String {
        if (reader != null) {
            Log.d(TAG, "connect " + reader!!.hostName)
            try {
                if (!reader!!.isConnected) {
                    // Establish connection to the RFID Reader
                    reader!!.connect()
                    configureReader()

                    //Call this function if the readerdevice supports scanner to setup scanner SDK
                    setupScannerSDK()
                    if (reader!!.isConnected) {
                        return "Connected: " + reader!!.hostName
                    }
                }
            } catch (e: InvalidUsageException) {
                e.printStackTrace()
            } catch (e: OperationFailureException) {
                e.printStackTrace()
                Log.d(TAG, "OperationFailureException " + e.vendorMessage)
                val des = e.results.toString()
                return "Connection failed " + e.vendorMessage + " " + des
            }
        }
        return ""
    }

    private fun configureReader() {
        Log.d(TAG, "configureReader " + reader!!.hostName)
        if (reader!!.isConnected) {
            val triggerInfo = TriggerInfo()
            triggerInfo.StartTrigger.triggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE
            triggerInfo.StopTrigger.triggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE
            try {
                // receive events from reader
                if (eventHandler == null) eventHandler = EventHandler()
                reader!!.Events.addEventsListener(eventHandler)
                // HH event
                reader!!.Events.setHandheldEvent(true)
                // tag event with tag data
                reader!!.Events.setTagReadEvent(true)
                reader!!.Events.setAttachTagDataWithReadEvent(false)
                // set trigger mode as rfid so scanner beam will not come
                reader!!.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true)
                // set start and stop triggers
                reader!!.Config.startTrigger = triggerInfo.StartTrigger
                reader!!.Config.stopTrigger = triggerInfo.StopTrigger
                // power levels are index based so maximum power supported get the last one
                maxPower = reader!!.ReaderCapabilities.transmitPowerLevelValues.size - 1
                // set antenna configurations
                val config = reader!!.Config.Antennas.getAntennaRfConfig(1)
                config.transmitPowerIndex = maxPower
                config.setrfModeTableIndex(0)
                config.tari = 0
                reader!!.Config.Antennas.setAntennaRfConfig(1, config)
                // Set the singulation control
                val s1SingulationControl = reader!!.Config.Antennas.getSingulationControl(1)
                s1SingulationControl.session = SESSION.SESSION_S0
                s1SingulationControl.Action.inventoryState = INVENTORY_STATE.INVENTORY_STATE_A
                s1SingulationControl.Action.slFlag = SL_FLAG.SL_ALL
                reader!!.Config.Antennas.setSingulationControl(1, s1SingulationControl)
                // delete any prefilters
                reader!!.Actions.PreFilters.deleteAll()
                //
            } catch (e: InvalidUsageException) {
                e.printStackTrace()
            } catch (e: OperationFailureException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupScannerSDK() {
        if (sdkHandler == null) {
            sdkHandler = SDKHandler(context)

            context.runOnUiThread {
                // For Bluetooth device
                val btResult = sdkHandler!!.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_LE)
                val btNormalResult = sdkHandler!!.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL)
                Log.d(TAG, "$btNormalResult results $btResult")

                sdkHandler!!.dcssdkSetDelegate(this)
                var notificationsMask = 0
                // We would like to subscribe to all scanner available/not-available events
                notificationsMask = notificationsMask or (
                        DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value or
                                DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value
                        )

                // We would like to subscribe to all scanner connection events
                notificationsMask = notificationsMask or (
                        DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value or
                                DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value or
                                DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value
                        )

                // Subscribe to events set in notification mask
                sdkHandler!!.dcssdkSubsribeForEvents(notificationsMask)
            }
        }

        // Ensure the SDKHandler works with available scanners
        if (sdkHandler != null) {
            val availableScanners: ArrayList<DCSScannerInfo> = sdkHandler!!.dcssdkGetAvailableScannersList() as ArrayList<DCSScannerInfo>
            scannerList!!.clear()
            for (scanner in availableScanners) {
                scannerList!!.add(scanner)
            }
        }

        // Try to establish communication with scanner
        if (reader != null) {
            for (device in scannerList!!) {
                if (device.scannerName.contains(reader!!.hostName)) {
                    try {
                        sdkHandler!!.dcssdkEstablishCommunicationSession(device.scannerID)
                        scannerID = device.scannerID
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    @Synchronized
    private fun disconnect() {
        Log.d(TAG, "Disconnect")
        try {
            if (reader != null) {
                if (eventHandler != null) reader!!.Events.removeEventsListener(eventHandler)
                if (sdkHandler != null) {
                    sdkHandler!!.dcssdkTerminateCommunicationSession(scannerID)
                    scannerList = null
                }
                reader!!.disconnect()
                context.sendToast("Disconnecting reader")
                //reader = null;
            }
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
        } catch (e: OperationFailureException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    private fun dispose() {
        disconnect()
        try {
            if (reader != null) {
                //Toast.makeText(getApplicationContext(), "Disconnecting reader", Toast.LENGTH_LONG).show();
                reader = null
                readers!!.Dispose()
                readers = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun performInventory() {
        try {
            reader!!.Actions.Inventory.perform()
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
        } catch (e: OperationFailureException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun stopInventory() {
        try {
            reader!!.Actions.Inventory.stop()
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
        } catch (e: OperationFailureException) {
            e.printStackTrace()
        }
    }

    // Read/Status Notify handler
    // Implement the RfidEventsLister class to receive event notifications
    inner class EventHandler : RfidEventsListener {
        // Read Event Notification
        override fun eventReadNotify(e: RfidReadEvents) {
            val myTags = reader!!.Actions.getReadTags(100)
            if (myTags != null) {
                for (index in myTags.indices) {
                    //  Log.d(TAG, "Tag ID " + myTags[index].getTagID());
                    Log.d(
                        TAG,
                        "Tag ID" + myTags[index].tagID + "RSSI value " + myTags[index].peakRSSI
                    )
                    Log.d(TAG, "RSSI value " + myTags[index].peakRSSI)
                    /* To get the RSSI value*/   //   Log.d(TAG, "RSSI value "+ myTags[index].getPeakRSSI());
                }
                AsyncDataUpdate().execute(arrayOf(*myTags))
            }
        }

        // Status Event Notification
        override fun eventStatusNotify(rfidStatusEvents: RfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.statusEventType)
            if (rfidStatusEvents.StatusEventData.statusEventType === STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.handheldEvent === HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                    object : AsyncTask<Void?, Void?, Void?>() {
                        override fun doInBackground(vararg voids: Void?): Void? {
                            Log.d(TAG, "HANDHELD_TRIGGER_PRESSED")
                            context.handleTriggerPress(true)
                            return null                        }
                    }.execute()
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.handheldEvent === HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    object : AsyncTask<Void?, Void?, Void?>() {
                        override fun doInBackground(vararg voids: Void?): Void? {
                            context.handleTriggerPress(false)
                            Log.d(TAG, "HANDHELD_TRIGGER_RELEASED")
                            return null                        }
                    }.execute()
                }
            }
            if (rfidStatusEvents.StatusEventData.statusEventType === STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
                object : AsyncTask<Void?, Void?, Void?>() {
                    override fun doInBackground(vararg voids: Void?): Void? {
                        TODO("Not yet implemented")
                    }
                }.execute()
            }
        }
    }

    private inner class AsyncDataUpdate :
        AsyncTask<Array<TagData?>?, Void?, Void?>() {
        override fun doInBackground(vararg params: Array<TagData?>?): Void? {
            context.handleTagdata(params[0])
            return null
        }
    }

    internal interface ResponseHandlerInterface {
        fun handleTagdata(tagData: Array<TagData?>?)
        fun handleTriggerPress(pressed: Boolean)
        fun barcodeData(`val`: String?)
        fun sendToast(`val`: String?) //void handleStatusEvents(Events.StatusEventData eventData);
    }

    companion object {
        const val TAG = "RFID_SAMPLE"
    }
}
