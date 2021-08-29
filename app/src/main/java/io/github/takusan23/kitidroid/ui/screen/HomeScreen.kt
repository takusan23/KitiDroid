package io.github.takusan23.kitidroid.ui.screen

import android.telephony.CellInfo
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import io.github.takusan23.kitidroid.network.NearCell
import io.github.takusan23.kitidroid.ui.component.AllCellInfoList
import io.github.takusan23.kitidroid.ui.component.HomeScreenTitle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * ホーム画面
 * */
@ExperimentalCoroutinesApi
@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cellInfoList = remember { mutableListOf<CellInfo>() }
    val latestUpdateMs = remember { mutableStateOf(System.currentTimeMillis()) }
    // 自動更新スイッチ
    val isAutoUpdate = remember { mutableStateOf(true) }

    if (isAutoUpdate.value) {
        // 基地局の情報をFlowで収集する
        val cellList = remember { NearCell.onNearCellCallBack(context, scope) }.collectAsState(initial = listOf()).value
        // 配列の更新。更新ボタンのために別に配列を用意する必要があったので
        remember(cellList) {
            latestUpdateMs.value = System.currentTimeMillis()
            cellInfoList.clear()
            cellInfoList.addAll(cellList)
        }
    }

    Scaffold(
        content = {
            Surface(color = MaterialTheme.colors.background) {

                LazyColumn(content = {

                    // タイトル部分
                    item {
                        HomeScreenTitle(
                            latestUpdateMs = latestUpdateMs.value,
                            onUpdateClick = {
                                // 手動更新押したとき
                                scope.launch {
                                    latestUpdateMs.value = System.currentTimeMillis()
                                    cellInfoList.clear()
                                    cellInfoList.addAll(NearCell.getNearCellList(context))
                                }
                            },
                            iaAutoUpdate = isAutoUpdate.value,
                            onAutoUpdate = { isAutoUpdate.value = it }
                        )
                    }

                    // 基地局一覧表示
                    item {
                        AllCellInfoList(cellInfoList)
                    }
                })

            }
        }
    )
}