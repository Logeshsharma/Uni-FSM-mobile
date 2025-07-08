package com.uni.fsm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uni.fsm.presentation.navigation.AppNavigationHost

import com.uni.fsm.ui.theme.UniFSMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniFSMTheme {
                AppNavigationHost()
            }
        }
    }
}
