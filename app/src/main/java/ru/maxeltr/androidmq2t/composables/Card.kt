package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.maxeltr.androidmq2t.R

@Composable
fun Card(
    data: String = "",
    name: String = "",
    time: String = "",
    onEditClick: () -> Unit,
    onPublishClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val TAG = "Card"
    val settingsString = stringResource(R.string.settings)
    val publishString = stringResource(R.string.publish)
    val editString = stringResource(R.string.edit)
    val deleteString = stringResource(R.string.delete)
    var expanded by remember { (mutableStateOf(false)) }

    Box(
        modifier = Modifier
            .background(Color.Gray)
            .height(150.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        expanded = true
                    },
                    onTap = {
                        onCardClick()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data,
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = time,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )

            }

            Button(
                shape = RectangleShape,
                modifier = Modifier
                    .width(80.dp)
                    .height(30.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                onClick = {
                    onPublishClick()
                }
            ) {
                Text(
                    fontSize = 10.sp,
                    text = publishString,

                    )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .background(Color.DarkGray),
        ) {
            DropdownMenuItem(
                onClick = {
                    onEditClick()
                    expanded = false
                },
                text = {
                    Text(
                        text = editString,
                        color = Color.White
                    )
                }
            )
            DropdownMenuItem(
                onClick = {
                    onDeleteClick()
                    expanded = false
                },
                text = {
                    Text(
                        text = deleteString,
                        color = Color.White
                    )
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CardPreview() {

    Card(
        data = "data",
        name = "name",
        time = "10:00:00 23.04.2025",
        onEditClick = {},
        onPublishClick = {},
        onDeleteClick = {},
        onCardClick = {},
    )
}