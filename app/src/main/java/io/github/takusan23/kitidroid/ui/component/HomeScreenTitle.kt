package io.github.takusan23.kitidroid.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.kitidroid.R
import java.text.SimpleDateFormat

/**
 * タイトル部分
 *
 * @param iaAutoUpdate 自動更新が有効ならtrue
 * @param onAutoUpdate 自動更新を有効、無効にしてほしいときに呼ばれる
 * @param latestUpdateMs 最終更新日時。ミリ秒
 * @param onUpdateClick 更新ボタン押したときに呼ばれる
 * */
@Composable
fun HomeScreenTitle(
    iaAutoUpdate: Boolean,
    onAutoUpdate: (Boolean) -> Unit,
    latestUpdateMs: Long,
    onUpdateClick: () -> Unit
) {
    val formatedDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(latestUpdateMs)
    Surface {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "近くの基地局",
                    fontSize = 25.sp
                )
                Text(text = "最終更新日時：$formatedDate")
                // 自動更新スイッチ
                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = "自動更新")
                    Spacer(modifier = Modifier.width(20.dp))
                    Switch(
                        checked = iaAutoUpdate,
                        onCheckedChange = onAutoUpdate
                    )
                }
            }
            IconButton(onClick = { onUpdateClick() }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_refresh_24), contentDescription = "update")
            }
        }
    }
}