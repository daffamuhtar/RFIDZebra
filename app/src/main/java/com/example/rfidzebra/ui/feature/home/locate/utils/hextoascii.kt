package com.example.rfidzebra.ui.feature.home.locate.utils


object hextoascii {
    val EXTENDED_ASCII_CHAR = charArrayOf(
        0x00C7.toChar(),
        0x00FC.toChar(),
        0x00E9.toChar(),
        0x00E2.toChar(),
        0x00E4.toChar(),
        0x00E0.toChar(),
        0x00E5.toChar(),
        0x00E7.toChar(),
        0x00EA.toChar(),
        0x00EB.toChar(),
        0x00E8.toChar(),
        0x00EF.toChar(),
        0x00EE.toChar(),
        0x00EC.toChar(),
        0x00C4.toChar(),
        0x00C5.toChar(),
        0x00C9.toChar(),
        0x00E6.toChar(),
        0x00C6.toChar(),
        0x00F4.toChar(),
        0x00F6.toChar(),
        0x00F2.toChar(),
        0x00FB.toChar(),
        0x00F9.toChar(),
        0x00FF.toChar(),
        0x00D6.toChar(),
        0x00DC.toChar(),
        0x00A2.toChar(),
        0x00A3.toChar(),
        0x00A5.toChar(),
        0x20A7.toChar(),
        0x0192.toChar(),
        0x00E1.toChar(),
        0x00ED.toChar(),
        0x00F3.toChar(),
        0x00FA.toChar(),
        0x00F1.toChar(),
        0x00D1.toChar(),
        0x00AA.toChar(),
        0x00BA.toChar(),
        0x00BF.toChar(),
        0x2310.toChar(),
        0x00AC.toChar(),
        0x00BD.toChar(),
        0x00BC.toChar(),
        0x00A1.toChar(),
        0x00AB.toChar(),
        0x00BB.toChar(),
        0x2591.toChar(),
        0x2592.toChar(),
        0x2593.toChar(),
        0x2502.toChar(),
        0x2524.toChar(),
        0x2561.toChar(),
        0x2562.toChar(),
        0x2556.toChar(),
        0x2555.toChar(),
        0x2563.toChar(),
        0x2551.toChar(),
        0x2557.toChar(),
        0x255D.toChar(),
        0x255C.toChar(),
        0x255B.toChar(),
        0x2510.toChar(),
        0x2514.toChar(),
        0x2534.toChar(),
        0x252C.toChar(),
        0x251C.toChar(),
        0x2500.toChar(),
        0x253C.toChar(),
        0x255E.toChar(),
        0x255F.toChar(),
        0x255A.toChar(),
        0x2554.toChar(),
        0x2569.toChar(),
        0x2566.toChar(),
        0x2560.toChar(),
        0x2550.toChar(),
        0x256C.toChar(),
        0x2567.toChar(),
        0x2568.toChar(),
        0x2564.toChar(),
        0x2565.toChar(),
        0x2559.toChar(),
        0x2558.toChar(),
        0x2552.toChar(),
        0x2553.toChar(),
        0x256B.toChar(),
        0x256A.toChar(),
        0x2518.toChar(),
        0x250C.toChar(),
        0x2588.toChar(),
        0x2584.toChar(),
        0x258C.toChar(),
        0x2590.toChar(),
        0x2580.toChar(),
        0x03B1.toChar(),
        0x00DF.toChar(),
        0x0393.toChar(),
        0x03C0.toChar(),
        0x03A3.toChar(),
        0x03C3.toChar(),
        0x00B5.toChar(),
        0x03C4.toChar(),
        0x03A6.toChar(),
        0x0398.toChar(),
        0x03A9.toChar(),
        0x03B4.toChar(),
        0x221E.toChar(),
        0x03C6.toChar(),
        0x03B5.toChar(),
        0x2229.toChar(),
        0x2261.toChar(),
        0x00B1.toChar(),
        0x2265.toChar(),
        0x2264.toChar(),
        0x2320.toChar(),
        0x2321.toChar(),
        0x00F7.toChar(),
        0x2248.toChar(),
        0x00B0.toChar(),
        0x2219.toChar(),
        0x00B7.toChar(),
        0x221A.toChar(),
        0x207F.toChar(),
        0x00B2.toChar(),
        0x25A0.toChar(),
        0x00A0.toChar()
    )

    fun convert(tag: String?): String? {
        return hex2ascii(tag)
    }

    fun isDatainHex(tagID: String): Boolean {
        val hex = tagID
        return if (tagID.startsWith("'") && tagID.endsWith("'")) false else true
    }

    private fun hex2ascii(tagID: String?): String? {
        return if (tagID != null && tagID != "") {
            val hex: String = tagID
            val n = hex.length
            if (n % 2 > 0) {
                return tagID
            }
            var sb = StringBuilder(n / 2)
            try {
                sb = StringBuilder(n / 2 + 2)
                //prefexing the ascii representation with a single quote
                sb.append("'")
                var i = 0
                while (i < n) {
                    val a = hex[i]
                    val b = hex[i + 1]
                    val c = (hexToInt(a) shl 4 or hexToInt(b)).toChar()
                    if (hexToInt(a) <= 7 && hexToInt(b) <= 0xf && c.code > 0x20 && c.code <= 0x7f) {
                        sb.append(c)
                    } else if (hexToInt(a) >= 8 && hexToInt(b) <= 0xf && c.code >= 0x80 && c.code <= 0xff) {
                        sb.append(EXTENDED_ASCII_CHAR[c.code - 0x7F])
                    } else {
                        sb.append(' ')
                    }
                    i += 2
                }
            } catch (iae: IllegalArgumentException) {
                return tagID
            } catch (abe: ArrayIndexOutOfBoundsException) {
                return tagID
            }
            //prefexing the ascii representation with a single quote
            sb.append("'")
            sb.toString()
        } else tagID
    }

    private fun hexToInt(ch: Char): Int {
        if ('a' <= ch && ch <= 'f') {
            return ch.code - 'a'.code + 10
        }
        if ('A' <= ch && ch <= 'F') {
            return ch.code - 'A'.code + 10
        }
        if ('0' <= ch && ch <= '9') {
            return ch.code - '0'.code
        }
        throw IllegalArgumentException(ch.toString())
    }
}
