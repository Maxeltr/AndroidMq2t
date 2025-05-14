package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel

@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    viewModel: Mq2tViewModel,
    navController: NavController
) {
    val TAG = "Dashboard"

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        val amount = viewModel.cards.size
        for (i in 0 until amount step 2) {
            CardRow(
                amount = amount,
                index = i,
                viewModel = viewModel,
                navController = navController
            )
        }
    }

}

@Composable
fun CardRow(
    modifier: Modifier = Modifier,
    amount: Int,
    index: Int,
    viewModel: Mq2tViewModel,
    navController: NavController
) {
    Row(
        modifier
            .background(Color.LightGray)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
        ) {
            CardTile(index, viewModel, navController)
        }
        if (index + 1 < amount) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            ) {
                CardTile(index = index + 1, viewModel = viewModel, navController = navController)
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }

    }

}

@Composable
fun CardTile(
    index: Int,
    viewModel: Mq2tViewModel,
    navController: NavController
) {
    val cardState: CardState? = viewModel.cards.getOrNull(index)

    Card(
        data = cardState?.subData ?: "",
        name = cardState?.name ?: "",
        time = cardState?.time ?: "",
        image = cardState?.subImagePreview,
        onEditClick = {
            navController.navigate("editCardView/${viewModel.cards.getOrNull(index)?.id ?: "-1"}")    // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
        },
        onPublishClick = {
            viewModel.onPublishClick(
                viewModel.cards.getOrNull(index)?.id ?: -1
            )        // TODO: If index is out of bounds, consider showing an error message or navigating to a default screen
        },
        onDeleteClick = {
            viewModel.onDeleteClick(viewModel.cards.getOrNull(index)?.id ?: -1)
        },
        onCardClick = {
            navController.navigate("fullScreenView/${viewModel.cards.getOrNull(index)?.id ?: "-1"}")
        }
    )
}