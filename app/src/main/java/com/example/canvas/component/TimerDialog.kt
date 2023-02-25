package com.example.canvas.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TimeInputDialog(
    onCloseDialog: () -> Unit,
    onSaveTime: (hours: Int, minutes: Int, seconds: Int) -> Unit
) {
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    val isSaveButtonEnabled = hour.isNotBlank() && minute.isNotBlank() && second.isNotBlank()

    fun isValidTime(hours: String, minutes: String, seconds: String): Boolean {
        val hh = hours.toIntOrNull() ?: return false
        val mm = minutes.toIntOrNull() ?: return false
        val ss = seconds.toIntOrNull() ?: return false

        return hh in 0..23 && mm in 0..59 && ss in 0..59
    }

    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        properties = DialogProperties(usePlatformDefaultWidth = true, dismissOnBackPress = false, dismissOnClickOutside = false),
        containerColor = Color.White,
        textContentColor = Color.Black,
        titleContentColor = Color.Black,
        title = { Text(text = "Enter Time") },
        onDismissRequest = {onCloseDialog},
        confirmButton = {
            TextButton(
                onClick = {
                    val isValidTime = isValidTime(hour, minute, second)
                    if (isValidTime) {
                        onSaveTime(hour.toInt(), minute.toInt(), second.toInt())
                        onCloseDialog()
                    }
                },
                enabled = isSaveButtonEnabled,
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black)
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCloseDialog,  colors = ButtonDefaults.buttonColors(contentColor = Color.Black)) {
                Text(text = "Cancel")
            }
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = hour,
                    onValueChange = { hour = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text(text = "HH") },
                    placeholder = { Text(text = "HH") },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black,  focusedLabelColor = Color.Black,  // Optional: focused border color
                        unfocusedLabelColor = Color.Black, ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = minute,
                    onValueChange = { minute = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text(text = "MM") },
                    placeholder = { Text(text = "MM") },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black,focusedLabelColor = Color.Black,  // Optional: focused border color
                        unfocusedLabelColor = Color.Black),
                    textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = second,
                    onValueChange = { second = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text(text = "SS") },
                    placeholder = { Text(text = "SS") },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black,focusedLabelColor = Color.Black,  // Optional: focused border color
                        unfocusedLabelColor = Color.Black),
                    textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
                )
            }
        },

        )
}
