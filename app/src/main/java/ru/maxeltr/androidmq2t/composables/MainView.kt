package ru.maxeltr.androidmq2t.composables

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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.maxeltr.androidmq2t.ui.theme.PurpleGrey80

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(innerPadding: PaddingValues = PaddingValues(0.dp)) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current

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

                        }) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Add"
                            )
                        }
                    },

                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            Dashboard(modifier = Modifier.padding(innerPadding))
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