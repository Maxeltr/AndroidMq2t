package ru.maxeltr.androidmq2t.composables

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Mq2tTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = Color.White
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White, // Цвет текста при фокусе
            unfocusedTextColor = Color.White, // Цвет текста при отсутствии фокуса
            focusedIndicatorColor = Color.DarkGray, // Цвет индикатора при фокусе
            unfocusedIndicatorColor = Color.LightGray, // Цвет индикатора при отсутствии фокуса
            focusedContainerColor = Color.Gray, // Цвет фона при фокусе
            unfocusedContainerColor = Color.Gray, // Цвет фона при отсутствии фокуса
            cursorColor = Color.White,
            selectionColors = TextSelectionColors(
                handleColor = Color.White, // Цвет "капельки" (маркер выделения)
                backgroundColor = Color.DarkGray // Цвет фона выделенного текста
            )
        )
    )
}