package io.github.takusan23.kitidroid.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.*
import io.github.takusan23.kitidroid.tool.PermissionCheckTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/** 近くの基地局取得 */
object NearCell {

    /**
     * 近くの基地局取得関数
     *
     * Android 10以降はコールバック形式での提供になってるのでサスペンド関数になってる
     * @param context Context
     * */
    @SuppressLint("MissingPermission")
    suspend fun getNearCellList(context: Context): List<CellInfo> {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10以降。権限があれば
            return suspendCoroutine {
                if (PermissionCheckTool.isGrantedLocationPermission(context)) {
                    telephonyManager.requestCellInfoUpdate(context.mainExecutor, object : TelephonyManager.CellInfoCallback() {
                        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
                            it.resume(cellInfo)
                        }
                    })
                } else {
                    it.resume(listOf())
                }
            }
        } else {
            // Android 9以前
            return telephonyManager.allCellInfo
        }
    }

    /**
     * 近くの基地局の情報が更新されたら呼ばれる。Flowなので定期的に値が流れてくると思います（LiveDataみたいな）。
     *
     * @param context Context
     * */
    @ExperimentalCoroutinesApi
    fun onNearCellCallBack(context: Context, coroutineScope: CoroutineScope) = callbackFlow {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 権限があるか
        if (PermissionCheckTool.isGrantedLocationPermission(context)) {

            // Android 12以降と分岐
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val callback = object : TelephonyCallback(), TelephonyCallback.CellInfoListener, TelephonyCallback.SignalStrengthsListener {
                    // 基地局の状態
                    override fun onCellInfoChanged(p0: MutableList<CellInfo>) {
                        trySend(p0)
                    }

                    // 電波強度
                    override fun onSignalStrengthsChanged(p0: SignalStrength) {
                        coroutineScope.launch { trySend(getNearCellList(context)) }
                    }
                }
                // 登録
                telephonyManager.registerTelephonyCallback(context.mainExecutor, callback)
                // flowがキャンセルされたら
                awaitClose {
                    telephonyManager.unregisterTelephonyCallback(callback)
                }
            } else {
                val callback = object : PhoneStateListener() {
                    @SuppressLint("MissingPermission")
                    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
                        super.onCellInfoChanged(cellInfo)
                        if (cellInfo != null) {
                            trySend(cellInfo)
                        }
                    }

                    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
                        super.onSignalStrengthsChanged(signalStrength)
                        coroutineScope.launch { trySend(getNearCellList(context)) }
                    }
                }
                // 登録
                telephonyManager.listen(callback, PhoneStateListener.LISTEN_CELL_INFO or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
                // flowがキャンセルされたら
                awaitClose {
                    telephonyManager.listen(callback, PhoneStateListener.LISTEN_NONE)
                }
            }
        }

    }

}