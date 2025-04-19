package ru.maxeltr.androidmq2t.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@Composable
fun Dashboard(modifier: Modifier = Modifier, viewModel: Mq2tViewModel, navController: NavController) {
    val TAG = "Dashboard"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Companion.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        val amount = viewModel.cards.size
        for (i in 0 until amount step 2) {
            CardRow(amount = amount, index = i, viewModel = viewModel, navController = navController)
        }
    }

}

@Composable
fun CardRow(modifier: Modifier = Modifier, amount: Int, index: Int, viewModel: Mq2tViewModel, navController: NavController) {
    Row(
        modifier
            .background(Color.Companion.LightGray)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Companion.Top
    ) {
        Box(
            modifier = Modifier.Companion
                .weight(1f)
                .padding(8.dp),

            ) {
            Card(
                data = viewModel.cards.getOrNull(index)?.subData ?: "",
                name = viewModel.cards.getOrNull(index)?.name ?: "",
                onSettingsClick = {
                    navController.navigate("editCardView/${viewModel.cards.getOrNull(index)?.id ?: "-1"}")    // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
                },
                onPublishClick = {
                    viewModel.onPublishClick(viewModel.cards.getOrNull(index)?.id ?: -1)        // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
                }
            )
        }

        if (index + 1 < amount) {
            Box(
                modifier = Modifier.Companion
                    .weight(1f)
                    .padding(8.dp),
            ) {
                Card(
                    data = viewModel.cards.getOrNull(index + 1)?.subData ?: "",
                    name = viewModel.cards.getOrNull(index)?.name ?: "",
                    onSettingsClick = {
                        navController.navigate("editCardView/${viewModel.cards.getOrNull(index + 1)?.id ?: "-1"}")      // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
                    },
                    onPublishClick = {
                        viewModel.onPublishClick(viewModel.cards.getOrNull(index + 1)?.id ?: -1)        // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier.Companion
                    .weight(1f)
                    .padding(8.dp)
            )
        }

    }

}