package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun Card(data: String = "", name: String = "") {
    Box(
        modifier = Modifier.Companion
            .background(Color.Companion.Gray)
            .height(150.dp)
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier.Companion
                .align(Alignment.Companion.TopCenter)
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Text(
                text = data,
                modifier = Modifier.Companion
                    .wrapContentSize()
                    .fillMaxWidth(),
                textAlign = TextAlign.Companion.Center,
                maxLines = 2,
                overflow = TextOverflow.Companion.Ellipsis
            )
            Spacer(modifier = Modifier.Companion.height(16.dp))
            Text(
                text = name,
                modifier = Modifier.Companion
                    .wrapContentSize()
                    .fillMaxWidth(),
                textAlign = TextAlign.Companion.Center,
                maxLines = 1,
                overflow = TextOverflow.Companion.Ellipsis
            )
            Spacer(modifier = Modifier.Companion.height(16.dp))


        }

        Button(
            shape = RectangleShape,
            modifier = Modifier.Companion
                .width(80.dp)
                .height(30.dp)
                .align(Alignment.Companion.BottomCenter)
                .offset(y = (-8).dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Companion.DarkGray,
                contentColor = Color.Companion.White
            ),
            onClick = { /* Действие для кнопки */ }
        ) {
            Text(
                fontSize = 10.sp,
                text = "Publish",

                )
        }
    }

}