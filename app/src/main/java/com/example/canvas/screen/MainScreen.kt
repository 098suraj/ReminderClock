package com.example.canvas.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.canvas.utils.Constants.BLUECOLOR
import com.example.canvas.viewModel.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TimerViewModel = hiltViewModel()) {
    val countDownStatus = viewModel.isCountingDown.collectAsState().value
    val forwardStatus = viewModel.forwardStatus.collectAsState().value
    val completeStatus = viewModel.complete.collectAsState().value
    val remainingTime by viewModel.remainingTime.collectAsState()
    var dropDownMenuStatus by remember { mutableStateOf(false) }
    var selectedOption by remember {
        mutableStateOf("Select tag")
    }
    viewModel.checkForwardStatus()
    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        CountdownWatch()
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = when {
                !forwardStatus -> "Tap on the circle above to set your time"
                completeStatus -> "Well done! You’ve achieved your goal."
                countDownStatus -> "Great going! You’ve completed ${((1 - remainingTime.toFloat() / 60) * 100).toInt()}% of your goal"
                else -> "You’ll be studying ${viewModel.getListString()} for ${viewModel.getHour()} hr ${viewModel.getMinutes()} min"
            }
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text(text = viewModel.getGoalText(), color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))
        if (countDownStatus || completeStatus) {
            Text(text = viewModel.getText(), color = Color.Black)
        } else {
            ExposedDropdownMenuBox(
                modifier = Modifier.wrapContentSize(),
                expanded = dropDownMenuStatus,
                onExpandedChange = {
                    dropDownMenuStatus = !dropDownMenuStatus
                }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOption,
                    onValueChange = { },
                    label = { Text("") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropDownMenuStatus,
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        containerColor = Color.White,
                        textColor = Color.Black,
                        focusedTrailingIconColor = BLUECOLOR,
                        unfocusedTrailingIconColor = BLUECOLOR,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(Color.White),
                    expanded = dropDownMenuStatus,
                    onDismissRequest = {
                        dropDownMenuStatus = false
                    }
                ) {
                    viewModel.getList().forEachIndexed { index, selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = "Study ${viewModel.getList()[index]}"
                                viewModel.setIndex(index)
                                dropDownMenuStatus = false
                            },
                            text = {
                                Text(
                                    text = selectionOption,
                                    style = TextStyle(color = Color.Black)
                                )
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(16.dp),
            onClick = {
                if (countDownStatus ||completeStatus) {
                    viewModel.resetCountdown()
                } else {
                    viewModel.startCountdown()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!countDownStatus) BLUECOLOR else Color.Red,
                contentColor = Color.White,
                disabledContainerColor = BLUECOLOR.copy(0.4f),
                disabledContentColor = Color.White.copy(0.7f)
            ),
            enabled = forwardStatus,
        ) {
            Text(text = if (countDownStatus) "End" else if (completeStatus) "New Goal" else "Start")
        }

    }
}