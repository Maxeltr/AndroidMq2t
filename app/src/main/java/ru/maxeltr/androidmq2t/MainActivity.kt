package ru.maxeltr.androidmq2t

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.maxeltr.androidmq2t.composables.EditCardView
import ru.maxeltr.androidmq2t.composables.MainView
import ru.maxeltr.androidmq2t.ui.theme.AndroidMq2tTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMq2tTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "mainView") {
                        composable("mainView") { MainView(innerPadding, navController) }
                        composable("editCardView") { EditCardView(id = 0) }
                    }

                }
            }
        }
    }
}


