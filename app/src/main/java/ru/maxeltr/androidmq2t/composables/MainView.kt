package ru.maxeltr.androidmq2t.composables

import android.app.Application
import android.content.Context
import android.util.Log
import ru.maxeltr.androidmq2t.R
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
import androidx.compose.ui.res.stringResource
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
import ru.maxeltr.androidmq2t.ui.theme.DarkGray
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: Mq2tViewModel,
    navController: NavController
) {
    val TAG = "MainView"
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current
    val isConnected = viewModel.isConnected.value
    val newCardString = stringResource(R.string.new_card)
    val connectionSettingsString = stringResource(R.string.connection_settings)

//    LaunchedEffect(Unit) {
//        viewModel.refreshConnectivityState()
//    }

//    val lifecycleOwner = LocalLifecycleOwner.current
//    val observer = rememberUpdatedState { event: Lifecycle.Event ->
//        if (event == Lifecycle.Event.ON_RESUME) {
//            if (!isConnected) {
//                Log.i(TAG, "State is disconnected on resume event. Try to reconnect.")
//                viewModel.connect()
//            }
//        }
//    }

//    DisposableEffect(lifecycleOwner) {
//        val lifecycleObserver = LifecycleEventObserver { _, event ->
//            observer.value(event)
//        }
//        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
//        }
//
//    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .requiredWidth(250.dp)
                    .fillMaxHeight()
            ) {
                DrawerContent { item ->
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    when (item) {
                        newCardString -> navController.navigate("editCardView/-1")
                        connectionSettingsString -> {navController.navigate("editConnectionView")}
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
                            stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.LightGray
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
                                contentDescription = stringResource(R.string.settings),
                                tint = Color.LightGray
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.refreshConnectivityState()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.add),
                                tint = Color.LightGray
                            )
                        }
                        Icon(
                            imageVector = if (isConnected) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = if (isConnected) stringResource(R.string.online) else stringResource(
                                R.string.offline
                            ),
                            tint = Color.LightGray
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
    val connectionSettingsString = stringResource(R.string.connection_settings)
    val newCardString = stringResource(R.string.new_card)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        TextButton(
            onClick = {
                onItemClick(newCardString)
            }
        ) {
            Text(
                stringResource(R.string.new_card),
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
                onItemClick(connectionSettingsString)
            }
        ) {
            Text(
                stringResource(R.string.connection_settings),
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
