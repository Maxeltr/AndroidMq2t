package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.R
import ru.maxeltr.androidmq2t.utils.IdGenerator
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@Composable
fun EditCardView(id: Int, viewModel: Mq2tViewModel, navController: NavController) {
    val context = LocalContext.current
    val idGenerator = remember { IdGenerator(context) }
    //val card: MutableState<CardState> = remember {mutableStateOf(viewModel.loadCardFromPreferences(id))}
    val card: MutableState<CardState> = remember { mutableStateOf(viewModel.getCardById(id)) }

    val idState = remember { mutableIntStateOf(card.value.id) }
    val nameState = remember { mutableStateOf(card.value.name) }
    val subTopicState = remember { mutableStateOf(card.value.subTopic) }
    val subQosState = remember { mutableIntStateOf(card.value.subQos) }
    val pubTopicState = remember { mutableStateOf(card.value.pubTopic) }
    val pubDataState = remember { mutableStateOf(card.value.pubData) }
    val pubQosState = remember { mutableIntStateOf(card.value.pubQos) }
    val pubRetainState = remember { mutableStateOf(card.value.pubRetain == true) }

    if (idState.intValue == -1) {
        idState.intValue = idGenerator.generateId()
    }

    val onSave = {
        //TODO viewModel.unsubscribe(card.value.subTopic)
        card.value = card.value.copy(
            id = idState.value,
            name = nameState.value,
            subTopic = subTopicState.value,
            subQos = subQosState.intValue,
            pubTopic = pubTopicState.value,
            pubData = pubDataState.value,
            pubQos = pubQosState.intValue,
            pubRetain = pubRetainState.value
        )
        viewModel.saveCardInPreferences(card.value)
        viewModel.subscribe(subTopicState.value, subQosState.intValue)
        navController.popBackStack()
    }

    val onCancel = {
        navController.popBackStack()
    }

    EditCardForm(
        nameState = nameState,
        subTopicState = subTopicState,
        subQosState = subQosState,
        pubTopicState = pubTopicState,
        pubDataState = pubDataState,
        pubQosState = pubQosState,
        pubRetainState = pubRetainState,
        onSave = { onSave() },
        onCancel = { onCancel() }
    )


}

@Composable
fun EditCardForm(
    nameState: MutableState<String>,
    subTopicState: MutableState<String>,
    subQosState: MutableState<Int>,
    pubTopicState: MutableState<String>,
    pubDataState: MutableState<String>,
    pubQosState: MutableState<Int>,
    pubRetainState: MutableState<Boolean>,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val expandedSubQos = remember { mutableStateOf(false) }
    val expandedPubQos = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(Color.Companion.Gray)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.card_settings),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Mq2tTextField(
                nameState.value,
                onValueChange = { newValue ->
                    nameState.value = newValue
                },
                label = stringResource(R.string.card_name)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                subTopicState.value,
                onValueChange = { newValue ->
                    subTopicState.value = newValue
                },
                label = stringResource(R.string.subscription_topic)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Box() {
                Mq2tDropdownQosMenu(subQosState, "Subscription QoS", "Qos")
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "Subscription QoS ${subQosState.value} ",
//                        color = Color.White
//                    )
//                    Box() {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowDropDown,
//                            contentDescription = "Dropdown Arrow",
//                            modifier = Modifier
//                                .clickable { expandedSubQos.value = true }
//                                .background(Color.Gray),
//                                tint = Color.White
//                        )
//                        DropdownMenu(
//                            expanded = expandedSubQos.value,
//                            onDismissRequest = { expandedSubQos.value = false },
//                            modifier = Modifier.background(Color.Gray)
//                        ) {
//                            listOf(0, 1, 2).forEach { qos ->
//                                DropdownMenuItem(
//                                    onClick = {
//                                        subQosState.value = qos
//                                        expandedSubQos.value = false
//                                    },
//                                    text = {
//                                        Text(
//                                            text = "QoS $qos",
//                                            color = Color.White
//                                        )
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                pubTopicState.value,
                onValueChange = { newValue ->
                    pubTopicState.value = newValue
                },
                label = stringResource(R.string.publication_topic)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Mq2tTextField(
                pubDataState.value,
                onValueChange = { newValue ->
                    pubDataState.value = newValue
                },
                label = stringResource(R.string.publication_data)
            )

            Box() {
                Mq2tDropdownQosMenu(pubQosState, "Publication QoS", "Qos")
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(text = "Publication QoS ${pubQosState.value} ")
//                    Box() {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowDropDown,
//                            contentDescription = "Dropdown Arrow",
//                            modifier = Modifier
//                                .clickable { expandedPubQos.value = true }
//                        )
//                        DropdownMenu(
//                            expanded = expandedPubQos.value,
//                            onDismissRequest = { expandedPubQos.value = false },
//                        ) {
//                            listOf(0, 1, 2).forEach { qos ->
//                                DropdownMenuItem(
//                                    onClick = {
//                                        pubQosState.value = qos
//                                        expandedPubQos.value = false
//                                    },
//                                    text = {
//                                        Text(text = "QoS $qos")
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.retain))
                Checkbox(
                    checked = pubRetainState.value,
                    onCheckedChange = { newValue ->
                        pubRetainState.value = newValue
                    }
                )
            }

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
fun EditCardViewPreview() {
    val nameState = remember { mutableStateOf("name test value") }
    val subTopicState = remember { mutableStateOf("subTopic/test/value") }
    val subQosState = remember { mutableStateOf(0) }
    val pubTopicState = remember { mutableStateOf("pubTopic/test/value") }
    val pubDataState = remember { mutableStateOf("pub test data") }
    val pubQosState = remember { mutableStateOf(0) }
    val pubRetainState = remember { mutableStateOf(false) }

    EditCardForm(
        nameState = nameState,
        subTopicState = subTopicState,
        subQosState = subQosState,
        pubTopicState = pubTopicState,
        pubDataState = pubDataState,
        pubQosState = pubQosState,
        pubRetainState = pubRetainState,
        onSave = {},
        onCancel = {},
    )
}

@Composable
fun Mq2tDropdownQosMenu (
    QosState: MutableState<Int>,
    label: String,
    menuText: String
) {
    val expandedQos = remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label ${QosState.value} ",
            color = Color.White
        )
        Box() {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier
                    .clickable { expandedQos.value = true }
                    .background(Color.Gray),
                tint = Color.White
            )
            DropdownMenu(
                expanded = expandedQos.value,
                onDismissRequest = { expandedQos.value = false },
                modifier = Modifier.background(Color.Gray)
            ) {
                listOf(0, 1, 2).forEach { qos ->
                    DropdownMenuItem(
                        onClick = {
                            QosState.value = qos
                            expandedQos.value = false
                        },
                        text = {
                            Text(
                                text = "$menuText $qos",
                                color = Color.White
                            )
                        }
                    )
                }
            }
        }
    }
}