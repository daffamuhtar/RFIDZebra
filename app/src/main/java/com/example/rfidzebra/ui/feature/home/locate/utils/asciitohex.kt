package com.example.rfidzebra.ui.feature.home.locate.utils

class asciitohex {

    companion object {
        fun convert(mData: String?): String? {
            if (mData != null) {
                if (hextoascii.isDatainHex(mData)) {
                    return mData
                }

                val modifiedData = mData.substring(1, mData.length - 1)
                val ch = modifiedData.toByteArray()
                val builder = StringBuilder()
                for (c in ch) {
                    builder.append(Integer.toHexString(c.toInt()))
                }
                return builder.toString()
            }
            return mData
        }
    }


}