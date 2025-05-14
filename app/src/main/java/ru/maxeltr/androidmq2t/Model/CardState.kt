package ru.maxeltr.androidmq2t.Model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

data class CardState(
    val id: Int = -1,
    val name: String = "",
    val subTopic: String = "",
    val subData: String = "",
    val subDataType: String = "text/plain",
    val subImage: ImageBitmap? = null,
    val subImagePreview: ImageBitmap? = null,
    val subQos: Int = 0,
    val subJsonpath: String = "",
    val pubTopic: String = "",
    val pubData: String = "",
    val pubQos: Int = 0,
    val pubRetain: Boolean = false,
    val time: String = "",
)