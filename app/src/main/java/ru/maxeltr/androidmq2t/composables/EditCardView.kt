package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel


@Composable
fun EditCardView(id: Int, viewModel: Mq2tViewModel, navController: NavController) {
    val card: MutableState<CardState> = remember {mutableStateOf(viewModel.loadCard(id))}
    //TODO show error message instead setting default values
    val nameState = remember { mutableStateOf(card.value.name) }
    val subTopicState = remember { mutableStateOf(card.value.subTopic) }
    val subQosState = remember { mutableIntStateOf(card.value.subQos) }
    val pubTopicState = remember { mutableStateOf(card.value.pubTopic) }
    val pubDataState = remember { mutableStateOf(card.value.pubData) }
    val pubQosState = remember { mutableIntStateOf(card.value.pubQos) }
    val pubRetainState = remember { mutableStateOf(card.value.pubRetain == true) }

    if (id != card.value.id) {
        throw IllegalArgumentException("The provided id ($id) does not match the card id (${card.value.id}).")
    }

    EditCardForm(
        nameState = nameState,
        subTopicState = subTopicState,
        subQosState = subQosState,
        pubTopicState = pubTopicState,
        pubDataState = pubDataState,
        pubQosState = pubQosState,
        pubRetainState = pubRetainState,
        onSave = {
            card.value = card.value.copy(
                name = nameState.value,
                subTopic = subTopicState.value,
                subQos = subQosState.value,
                pubTopic = pubTopicState.value,
                pubData = pubDataState.value,
                pubQos = pubQosState.value,
                pubRetain = pubRetainState.value
            )
            viewModel.saveCard(card.value)
            navController.popBackStack()
        },
        onCancel = {
            navController.popBackStack()
        },
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
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Card settings")

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nameState.value,
                onValueChange = { newValue ->
                    nameState.value = newValue
                },
                label = {
                    Text("Card name")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Subscription settings")

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = subTopicState.value,
                onValueChange = { newValue ->
                    subTopicState.value = newValue
                },
                label = {
                    Text("Subscription topic")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Box() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Subscription QoS ${subQosState.value} ")
                    Box() {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            modifier = Modifier
                                .clickable { expandedSubQos.value = true }
                        )
                        DropdownMenu(
                            expanded = expandedSubQos.value,
                            onDismissRequest = { expandedSubQos.value = false },

                            ) {
                            listOf(0, 1, 2).forEach { qos ->
                                DropdownMenuItem(
                                    onClick = {
                                        subQosState.value = qos
                                        expandedSubQos.value = false
                                    },
                                    text = {
                                        Text(text = "QoS $qos")
                                    }
                                )
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Publication settings")

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = pubTopicState.value,
                onValueChange = { newValue ->
                    pubTopicState.value = newValue
                },
                label = {
                    Text("Publication topic")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = pubDataState.value,
                onValueChange = { newValue ->
                    pubDataState.value = newValue
                },
                label = {
                    Text("Publication data")
                }
            )

            Box() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Publication QoS ${pubQosState.value} ")
                    Box() {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            modifier = Modifier
                                .clickable { expandedPubQos.value = true }
                        )
                        DropdownMenu(
                            expanded = expandedPubQos.value,
                            onDismissRequest = { expandedPubQos.value = false },
                        ) {
                            listOf(0, 1, 2).forEach { qos ->
                                DropdownMenuItem(
                                    onClick = {
                                        pubQosState.value = qos
                                        expandedPubQos.value = false
                                    },
                                    text = {
                                        Text(text = "QoS $qos")
                                    }
                                )
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Retain")
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
                //Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onSave()
                    }) {
                    Text("Save")
                }

                //Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onCancel()
                    }) {
                    Text("Cancel")
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