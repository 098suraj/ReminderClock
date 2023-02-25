package com.example.canvas.utils

import androidx.compose.ui.graphics.Color

object Constants {
    const val TIMER="timer"
    val BLUECOLOR= Color(0xFF1589EE)
}
fun Int.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}