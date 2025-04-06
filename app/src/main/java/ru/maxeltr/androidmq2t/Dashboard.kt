package ru.maxeltr.myapp

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun Dashboard(modifier: Modifier = Modifier, amount: Int = 3) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        for (i in 0 until amount step 2) {
            CardRow(amount = amount, index = i)
        }
    }

}

@Composable
fun CardRow(modifier: Modifier = Modifier, amount: Int, index: Int) {
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
            Card()
        }

        if (index + 1 < amount) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            ) {
                Card()
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