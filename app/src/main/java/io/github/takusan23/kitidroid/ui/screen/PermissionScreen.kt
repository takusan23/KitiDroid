package io.github.takusan23.kitidroid.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 権限取得画面
 *
 * @param onHome ホーム画面に遷移してほしいときに呼ばれる
 * */
@Composable
fun PermissionScreen(onHome: () -> Unit) {
    val context = LocalContext.current

    // 権限コールバック
    val permissionResult = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = { resultList ->
        if (resultList.all { it.value == true }) {
            Toast.makeText(context, "権限の付与に成功しました。ご協力ありがとうございます。", Toast.LENGTH_SHORT).show()
            onHome()
        }
    })

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "権限付与のお願い") })
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "近くの基地局の取得には以下の権限が必要です。")
                Text(
                    text = """
                    ・位置情報
                    ・電話の状態への読み取りアクセス
                """.trimIndent()
                )
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = { permissionResult.launch(arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE
                    )) }
                ) {
                    Text(text = "権限付与")
                }
            }
        }
    )
}