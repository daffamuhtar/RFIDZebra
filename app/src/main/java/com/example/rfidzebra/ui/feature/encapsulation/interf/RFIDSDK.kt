package com.example.rfidzebra.ui.feature.encapsulation.interf

interface RFIDSDK {
    fun connectReader(): String
    fun disconnectReader(): String
}
