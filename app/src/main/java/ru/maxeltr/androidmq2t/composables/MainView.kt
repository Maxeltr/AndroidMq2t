package ru.maxeltr.androidmq2t.composables

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.maxeltr.androidmq2t.ui.theme.PurpleGrey80
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(innerPadding: PaddingValues = PaddingValues(0.dp), navController: NavController, viewModel: Mq2tViewModel) {
    val TAG = "MainView"
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current
    val isConnected = viewModel.isConnected.value

    LaunchedEffect(Unit) {
        viewModel.refreshConnectivity()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val observer = rememberUpdatedState { event: Lifecycle.Event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            // Обновление данных при возвращении на экран
            if (isConnected) {
                Log.i(TAG, "Try to reconnect.")
                viewModel.connect()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            observer.value(event)
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier
                .requiredWidth(250.dp)
                .fillMaxHeight()
            ) {
                DrawerContent { item ->
                    // Обработка нажатия на элемент меню
                    println("Clicked on $item")
                    Toast.makeText(context, "Clicked on $item", Toast.LENGTH_SHORT).show()

                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.DarkGray,
                    ),
                    title = {
                        Text(
                            "Mq2t",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = PurpleGrey80
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Options"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.refreshConnectivity()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Add"
                            )
                        }
                        Icon(
                            imageVector = if (isConnected) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = if (isConnected) "Online" else "Offline"
                        )
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            Dashboard(modifier = Modifier.padding(innerPadding), viewModel, navController)
        }
    }
}

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                onItemClick("Item 1")
            }
        ) {
            Text("Item______________1",
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        TextButton(
            onClick = {
                onItemClick("Item 2")
            }
        ) {
            Text("Item  d  2",
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        TextButton(
            onClick = {
                onItemClick("Item 3")
            }
        ) {
            Text("Item                              3",
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
