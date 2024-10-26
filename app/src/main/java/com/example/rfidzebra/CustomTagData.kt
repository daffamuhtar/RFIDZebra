package com.example.rfidzebra

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS
import com.zebra.rfid.api3.AccessOperationResult
import com.zebra.rfid.api3.GEN2V2_OPERATION_CODE
import com.zebra.rfid.api3.GEN2V2_OPERATION_STATUS
import com.zebra.rfid.api3.LocationInfo
import com.zebra.rfid.api3.MEMORY_BANK
import com.zebra.rfid.api3.SYSTEMTIME
import com.zebra.rfid.api3.SeenTime

data class CustomTagData(
    var LocationInfo: LocationInfo? = null,
    var MultiTagLocateInfo: Any? = null,
    var SeenTime: SeenTime? = null,
    var AccessOperationResult: AccessOperationResult? = null,
    var m_sTagID: String? = null,
    var m_nTagIDAllocated: Int = 0,
    var m_PC: Int = 0,
    var m_XPC: Int = 0,
    var m_CRC: Int = 0,
    var m_sCRC: String? = null,
    var m_AntennaID: Short = 0,
    var m_PeakRSSI: Short = 0,
    var m_PhaseInfo: Short = 0,
    var m_ChannelIndex: Short = 0,
    var m_Channel: String? = null,
    var m_TagSeenCount: Int = 0,
    var m_nNumWordsWritten: Int = 0,
    var m_eOpCode: ACCESS_OPERATION_CODE? = null,
    var m_eG2v2OpCode: GEN2V2_OPERATION_CODE? = null,
    var m_eOpStatus: ACCESS_OPERATION_STATUS? = null,
    var m_eG2v2Status: GEN2V2_OPERATION_STATUS? = null,
    var m_eMemoryBank: MEMORY_BANK? = null,
    var m_MemoryBankData: String? = null,
    var m_ResponseData: String? = null,
    var m_nMemoryBankDataOffset: Int = 0,
    var m_nMemoryBankDataAllocated: Int = 0,
    var m_tagEvent: Any? = null,
    var m_tagEventTimeStamp: SYSTEMTIME? = null,
    var m_bContainsLocationInfo: Boolean = false,
    var m_bContainsMultiTagLocateInfo: Boolean = false,
    var m_PermaLockData: String? = null,
    var m_brandIDStatus: Boolean = false,
    var m_TID: String? = null,
    var m_User: String? = null,
    var channelFreq: String? = null,
    var m_brandValid: Short = 0,
    var AccessOptErrorCode: Int = 0,
    val access_operation_status: ACCESS_OPERATION_STATUS? = null,
    val CRC: Int = 0,
    val m_QTControlData: Short = 0,

    var memoryBankUser: String? = null,
    var memoryBankEPC: String? = null,
    var memoryBankReserved: String? = null,
    var memoryBankTID: String? = null,

    )
