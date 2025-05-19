package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.maxeltr.androidmq2t.Model.ConnectionState
import ru.maxeltr.androidmq2t.R
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@Composable
fun EditConnectionView(viewModel: Mq2tViewModel, navController: NavController) {

    val connection: MutableState<ConnectionState> =
        remember { mutableStateOf(viewModel.loadConnectionFromPreferences()) }
    val host = remember { mutableStateOf(connection.value.host) }
    val port = remember { mutableStateOf(connection.value.port) }
    val username = remember { mutableStateOf(connection.value.username) }
    val password = remember { mutableStateOf(connection.value.password) }
    var isNavigating = remember { mutableStateOf(false) }

    EditConnectionForm(
        host = host,
        port = port,
        username = username,
        password = password,
        onSave = {
            if (!isNavigating.value) {
                isNavigating.value = true
                connection.value = connection.value.copy(
                    host = host.value,
                    port = port.value,
                    username = username.value,
                    password = password.value,
                )
                viewModel.saveConnectionInPreferences(connection.value)
                viewModel.reconnect()
                navController.popBackStack()
            }
        },
        onCancel = {
            if (!isNavigating.value) {
                isNavigating.value = true
                navController.popBackStack()
            }
        },
    )


}

@Composable
fun EditConnectionForm(
    host: MutableState<String>,
    port: MutableState<String>,
    username: MutableState<String>,
    password: MutableState<String>,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.connection_settings),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                value = host.value,
                onValueChange = { newValue ->
                    host.value = newValue
                },
                label = stringResource(R.string.host)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                value = port.value,
                onValueChange = { newValue ->
                    port.value = newValue
                },
                label = stringResource(R.string.port)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                value = username.value,
                onValueChange = { newValue ->
                    username.value = newValue
                },
                label = stringResource(R.string.username)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                value = password.value,
                onValueChange = { newValue ->
                    password.value = newValue
                },
                label = stringResource(R.string.password)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Companion.DarkGray,
                        contentColor = Color.Companion.White
                    ),
                    onClick = {
                        onSave()
                    }) {
                    Text(stringResource(R.string.save))
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Companion.DarkGray,
                        contentColor = Color.Companion.White
                    ),
                    onClick = {
                        onCancel()
                    }) {
                    Text(stringResource(R.string.cancel))
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditConnectionViewPreview() {
    val host = remember { mutableStateOf("host test value") }
    val port = remember { mutableStateOf("port test value") }
    val username = remember { mutableStateOf("username test value") }
    val password = remember { mutableStateOf("password test value") }

    EditConnectionForm(
        host = host,
        port = port,
        username = username,
        password = password,
        onSave = {},
        onCancel = {},
    )
}