package io.github.takusan23.kitidroid.ui.component

import android.os.Build
import android.telephony.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.kitidroid.network.BandTool
import io.github.takusan23.kitidroid.network.CellInfoReflection
import io.github.takusan23.kitidroid.network.CellType

/** 基地局一覧表示。5Gとかもまとめて表示 */
@Composable
fun AllCellInfoList(cellInfoList: List<CellInfo>) {
    // NR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val nrCellList = cellInfoList.filterIsInstance(CellInfoNr::class.java)
        CellInfoList(nrCellList)
    }
    // LTE
    val lteCellList = cellInfoList.filterIsInstance(CellInfoLte::class.java)
    CellInfoList(lteCellList)
    // W-CDMA (3G)
    val wcdmaCellList = cellInfoList.filterIsInstance(CellInfoWcdma::class.java)
    CellInfoList(wcdmaCellList)
}

/** 基地局を一覧表示する。CellIdentityを */
@Composable
fun CellInfoList(cellInfoList: List<CellInfo>) {
    if (cellInfoList.isNotEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Surface(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colors.primary.copy(0.2f)
        ) {
            Column {
                Text(
                    text = "${CellType.getType(cellInfoList[0])} - ${cellInfoList.size}台",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )

                cellInfoList.forEach { cellInfo ->

                    val band = when {
                        cellInfo is CellInfoLte -> BandTool.earfcnToBand(cellInfo.cellIdentity.earfcn)
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr -> BandTool.nrarfcnToBand((cellInfo.cellIdentity as CellIdentityNr).nrarfcn)
                        else -> 0
                    }

                    // CellIdentityの各メソッドを呼ぶ
                    val cellIdentityInvokeResult = CellInfoReflection.dynamicCellIdentityMethodInvoke(cellInfo).toMutableList()
                    // Bandの情報まで入っていないので
                    cellIdentityInvokeResult.add(Pair("Band", band))

                    // CellSignalStrengthの各メソッドも呼ぶ
                    val signalStrengthInvokeResult = CellInfoReflection.dynamicCellSignalStrengthMethodInvoke(cellInfo)

                    val identityNameList = cellIdentityInvokeResult.map { it.first }
                    val identityValueList = cellIdentityInvokeResult.map { it.second }
                    val signalStrengthNameList = signalStrengthInvokeResult.map { it.first }
                    val signalStrengthValueList = signalStrengthInvokeResult.map { it.second }

                    Divider()
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                                .weight(2f),
                            text = identityNameList.joinToString(separator = "\n") { it },
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, end = 10.dp),
                            textAlign = TextAlign.End,
                            text = identityValueList.joinToString(separator = "\n") { it.toString() },
                        )
                    }
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(2f),
                            text = signalStrengthNameList.joinToString(separator = "\n") { it },
                        )
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            textAlign = TextAlign.End,
                            text = signalStrengthValueList.joinToString(separator = "\n") { it.toString() },
                        )
                    }
                }
            }
        }
    }
}
