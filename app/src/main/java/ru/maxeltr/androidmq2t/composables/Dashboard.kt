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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModelFactory

@Preview(showBackground = true)
@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    val TAG = "Dashboard"
    val viewModel: Mq2tViewModel = viewModel(factory = Mq2tViewModelFactory(LocalContext.current))
    val cards = viewModel.cards


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Companion.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        val amount = cards.size
        Log.v(TAG, "amount = $amount!.")
        for (i in 0 until amount step 2) {
            CardRow(amount = amount, index = i, cardStates = cards)
        }
    }

}

@Composable
fun CardRow(modifier: Modifier = Modifier, amount: Int, index: Int, cardStates: List<CardState>) {
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
            Card(cardStates[index].subData, cardStates[index].name)
        }

        if (index + 1 < amount) {
            Box(
                modifier = Modifier.Companion
                    .weight(1f)
                    .padding(8.dp),
            ) {
                Card(cardStates[index].subData, cardStates[index].name)
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