package ru.maxeltr.androidmq2t

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.maxeltr.androidmq2t.composables.EditCardView
import ru.maxeltr.androidmq2t.composables.MainView
import ru.maxeltr.androidmq2t.ui.theme.AndroidMq2tTheme
import ru.maxeltr.androidmq2t.composables.EditConnectionView
import ru.maxeltr.androidmq2t.composables.FullScreenView
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMq2tTheme {
                val navController = rememberNavController()
                val viewModel: Mq2tViewModel =
                    viewModel(factory = Mq2tViewModelFactory(LocalContext.current))
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "mainView"
                    ) {
                        composable("mainView") {
                            MainView(innerPadding, viewModel, navController)
                        }
                        composable("editCardView/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                            EditCardView(id = id, viewModel, navController)
                        }
                        composable("editConnectionView") {
                            EditConnectionView(viewModel, navController)
                        }
                        composable("fullScreenView/{id}") { backStackEntry ->
                            val id = backStackEntry . arguments ?. getString ("id")?.toInt() ?: -1
                            FullScreenView(id = id, viewModel, navController)
                        }
                    }

                }
            }
        }
    }
}


