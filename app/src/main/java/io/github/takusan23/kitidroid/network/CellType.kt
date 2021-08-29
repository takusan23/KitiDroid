package io.github.takusan23.kitidroid.network

import android.os.Build
import android.telephony.*

object CellType {

    /** CellInfoからなんの種類(LTE/3G)を割り出す */
    fun getType(cellInfo: CellInfo): String {
        return when {
            cellInfo is CellInfoLte -> "LTE - 4G"
            cellInfo is CellInfoCdma -> "CDMA - 3G"
            cellInfo is CellInfoGsm -> "GSM - 2G"
            cellInfo is CellInfoWcdma -> "W-CDMA - 3G"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> "NR - 5G"
            else -> "不明"
        }
    }

}