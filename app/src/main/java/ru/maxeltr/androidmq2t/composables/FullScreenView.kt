package ru.maxeltr.androidmq2t.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import ru.maxeltr.androidmq2t.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.Model.ConnectionState

import ru.maxeltr.androidmq2t.utils.IdGenerator
import ru.maxeltr.androidmq2t.viewmodel.Mq2tViewModel
import kotlin.String
import kotlin.math.log

@Composable
fun FullScreenView(
    id: Int,
    viewModel: Mq2tViewModel,
    navController: NavController
) {
    val card: MutableState<CardState> = remember {mutableStateOf(viewModel.getCardById(id))}

    val dataState = remember { mutableStateOf(card.value.subData) }
    val nameState = remember { mutableStateOf(card.value.name) }
    val timeState = remember { mutableStateOf(card.value.time) }
    val imageState = remember { mutableStateOf(card.value.subImage) }

    FullScreenForm(
        data = dataState,
        name = nameState,
        time = timeState,
        image = imageState,
        navController,
    )


}

@Composable
fun FullScreenForm(
    data: MutableState<String>,
    name: MutableState<String>,
    time: MutableState<String>,
    image: MutableState<ImageBitmap?>,
    navController: NavController
) {
    val initialScale = 1f
    var scale = remember { mutableFloatStateOf(initialScale) }
    var offsetX = remember { mutableFloatStateOf(0f) }
    var offsetY = remember { mutableFloatStateOf(0f) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    Log.d("panAndZoom", "density=$density, screenWidth=$screenWidth, screenHeight=$screenHeight")

                    scale.floatValue = (scale.floatValue * zoom).coerceAtLeast(initialScale)
                    Log.d("panAndZoom", "scale=${scale.floatValue}")

                    if (scale.floatValue > initialScale) {
                        offsetX.floatValue += pan.x
                        offsetY.floatValue += pan.y


                        Log.d("panAndZoom", "offsetX=${offsetX.floatValue}, offsetY=${offsetY.floatValue}")


                    }
                }
            },
        contentAlignment = Alignment.Center
        ) {
        image.value?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                //contentScale = ContentScale.Crop,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale.floatValue,
                        scaleY = scale.floatValue,
                        translationX = offsetX.floatValue,
                        translationY = offsetY.floatValue
                    )
            )

//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f))
//            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name.value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = data.value,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = time.value,
                fontSize = 12.sp,
                color = Color.White
            )

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun FullScreenViewPreview() {
//    FullScreenForm(
//        data = remember { mutableStateOf("sub test data") },
//        name = remember { mutableStateOf("name test value") },
//        time = remember { mutableStateOf("10:00:00 24.04.2025") },
//        navController = rememberNavController()
//    )
//}