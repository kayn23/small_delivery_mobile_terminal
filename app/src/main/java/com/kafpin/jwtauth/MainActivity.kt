package com.kafpin.jwtauth

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.data.dataStore.RoleManager
import com.kafpin.jwtauth.data.dataStore.StockInfoManager
import com.kafpin.jwtauth.data.dataStore.TokenManager
import com.kafpin.jwtauth.ui.MyApplication
import com.kafpin.jwtauth.ui.theme.JwtAuthTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var roleManager: RoleManager
    @Inject
    lateinit var stockInfoManager: StockInfoManager
    @Inject
    lateinit var ipServerManager: IpServerManager

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        enableEdgeToEdge()
        setContent {
            JwtAuthTheme(darkTheme = true) {
                MaterialTheme {
                    MyApplication(tokenManager, roleManager, stockInfoManager, ipServerManager)
                }
            }
        }
    }
}