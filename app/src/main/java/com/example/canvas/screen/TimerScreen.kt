package com.example.canvas.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.canvas.component.TimeInputDialog
import com.example.canvas.utils.Constants.BLUECOLOR
import com.example.canvas.utils.formatTime
import com.example.canvas.viewModel.TimerViewModel
import kotlin.time.Duration


@Composable
fun CountdownWatch(viewModel: TimerViewModel = hiltViewModel() ) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val status=viewModel.isCountingDown.collectAsState().value
    val completeStatus=viewModel.complete.collectAsState().value
    var dialogStatus by remember {
        mutableStateOf(false)
    }
    val canvasSize = 200.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .clickable {
                if (!status) {
                    dialogStatus = true
                }
            }
    ) {
        Canvas(
            modifier = Modifier.size(canvasSize)
        ) {
            val strokeWidth = size.minDimension * 0.05f
            val sweepAngle = 360 * (1 - remainingTime.toFloat() / 60)
            drawCircle(
                color = Color.Gray,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = if(status || completeStatus) BLUECOLOR else Color.Gray,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }
        Text(
            text = remainingTime.formatTime(),
            style = MaterialTheme.typography.bodySmall
        )
    }
    if(
       dialogStatus
    )
    TimeInputDialog(onCloseDialog = { dialogStatus=false  }, onSaveTime ={hours, minutes, seconds ->
        viewModel.checkForwardStatus()
        viewModel.convertToSeconds(hours,minutes,seconds)
    })
}

