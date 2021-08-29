package io.github.takusan23.kitidroid.network

import android.os.Build
import android.telephony.*
import java.lang.Exception

/**
 * CellInfoの中身をリフレクションを使って動的に取得する
 * */
object CellInfoReflection {

    /**
     * リフレクションを使ってCellIdentityのメソッドを動的に呼びます。MapのSecondはStringかIntになります。
     * @param cellInfo CellInfo
     * @return Pairの一番目はメソッド名、二番目はメソッドを呼んだ返り値
     * */
    fun dynamicCellIdentityMethodInvoke(cellInfo: CellInfo): List<Pair<String, Any>> {
        val methods = when {
            cellInfo is CellInfoLte -> cellInfo.cellIdentity::class.java.methods
            cellInfo is CellInfoWcdma -> cellInfo.cellIdentity::class.java.methods
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> cellInfo.cellIdentity::class.java.methods
            else -> return listOf()
        }
        val cellIdentity = when {
            cellInfo is CellInfoLte -> cellInfo.cellIdentity
            cellInfo is CellInfoWcdma -> cellInfo.cellIdentity
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> cellInfo.cellIdentity
            else -> return listOf()
        }
        return methods
            .filter {
                // いらないメソッドを消す
                !listOf(
                    "toString",
                    "hashCode"
                ).contains(it.name)
            }
            .mapNotNull {
                // 引数が必要な場合は例外出すのでtry-catch
                try {
                    val result = it.invoke(cellIdentity)
                    // 数値 or 文字列 のみ受け取る
                    if (result is String || result is Int) {
                        // メソッド名先頭に付いてるgetはいらない
                        Pair(it.name.replace("get", ""), result)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
    }

    /**
     * リフレクションを使ってCellSignalStrengthのメソッドを動的に呼びます。PairのSecondはStringかIntになります。
     * @param cellInfo CellInfo
     * @return Pairの一番目はメソッド名、二番目はメソッドを呼んだ返り値
     * */
    fun dynamicCellSignalStrengthMethodInvoke(cellInfo: CellInfo): List<Pair<String, Any>> {
        val methods = when {
            cellInfo is CellInfoLte -> cellInfo.cellSignalStrength::class.java.methods
            cellInfo is CellInfoWcdma -> cellInfo.cellSignalStrength::class.java.methods
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> cellInfo.cellSignalStrength::class.java.methods
            else -> return listOf()
        }
        val cellSignalStrength = when {
            cellInfo is CellInfoLte -> cellInfo.cellSignalStrength
            cellInfo is CellInfoWcdma -> cellInfo.cellSignalStrength
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> cellInfo.cellSignalStrength
            else -> return listOf()
        }
        return methods
            .filter {
                // いらないメソッドを消す
                !listOf(
                    "toString",
                    "hashCode"
                ).contains(it.name)
            }
            .mapNotNull {
                // 引数が必要な場合は例外出すのでtry-catch
                try {
                    val result = it.invoke(cellSignalStrength)
                    // 数値 or 文字列 のみ受け取る
                    if (result is String || result is Int) {
                        // メソッド名先頭に付いてるgetはいらない
                        Pair(it.name.replace("get", ""), result)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
    }


}