package com.kafpin.jwtauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.ui.MyApplication
import com.kafpin.jwtauth.ui.screens.LoginScreen
import com.kafpin.jwtauth.ui.theme.JwtAuthTheme
import com.kafpin.jwtauth.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            JwtAuthTheme(darkTheme = true) {
                MaterialTheme {
                    MyApplication(tokenManager)
                }
            }
        }
    }
}