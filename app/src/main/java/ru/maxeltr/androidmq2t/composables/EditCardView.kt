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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.viewmodel.EditCardViewModel
import ru.maxeltr.androidmq2t.viewmodel.EditCardViewModelFactory
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModelFactory
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun EditCardView(id: Int) {
    val viewModel: EditCardViewModel = viewModel(factory = EditCardViewModelFactory(LocalContext.current))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit card")

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = "viewModel",
            onValueChange = { newValue ->
                //viewModel.subTopic = it
                Log.v("EditCardView", newValue)
            },

        )


    }
}

@Preview(showBackground = true)
@Composable
fun EditCardViewP() {
    EditCardView(id = 0)
}