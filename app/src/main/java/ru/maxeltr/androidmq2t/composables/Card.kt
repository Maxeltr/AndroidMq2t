package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
        modifier = Modifier.Companion
            .background(Color.Companion.Gray)
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
            modifier = Modifier.Companion
                .align(Alignment.Companion.TopCenter)
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
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
        }

//        IconButton(
//            onClick = {
//                onSettingsClick()
//            },
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(4.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Settings,
//                contentDescription = settingsString,
//                tint = Color.White
//            )
//        }

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
            onClick = {
                onPublishClick()
            }
        ) {
            Text(
                fontSize = 10.sp,
                text = publishString,

                )
        }

        Text(
            text = time,
            fontSize = 10.sp,
            modifier = Modifier.Companion
                .wrapContentSize()
                .fillMaxWidth(),
            textAlign = TextAlign.Companion.Center,
            maxLines = 1,
            overflow = TextOverflow.Companion.Ellipsis
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    onEditClick()
                    expanded = false
                },
                text = {
                    Text(text = editString)
                }
            )
            DropdownMenuItem(
                onClick = {
                    onDeleteClick()
                    expanded = false
                },
                text = {
                    Text(text = deleteString)
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