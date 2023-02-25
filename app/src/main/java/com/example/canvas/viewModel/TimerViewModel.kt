package com.example.canvas.viewModel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private val hour = MutableStateFlow(0)
    private val minutes = MutableStateFlow(0)
    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime
    private val _complete = MutableStateFlow(false)
    var complete = _complete.asStateFlow()
    val isCountingDown = MutableStateFlow(false)
    private val _timeSaved = MutableStateFlow(false)
    private val listItemSelected = MutableStateFlow(false)
    private val selectedIndex = MutableStateFlow(0)
    private val _forwardStatus = MutableStateFlow(false)
    val forwardStatus = _forwardStatus.asStateFlow()
    val string=MutableStateFlow("")
    private var countdownJob: Job? = null

    private val list = listOf(
        "Physics",
        "Maths",
        "English"
    )

    fun checkForwardStatus() {
        _forwardStatus.value = _timeSaved.value && listItemSelected.value
    }

    fun getList(): List<String> {
        return list
    }
    fun getListString():String{
        return  list[selectedIndex.value]
    }
    fun getHour():Int{
        return  hour.value
    }
    fun getMinutes():Int{
        return  minutes.value
    }
    fun setIndex(index: Int) {
        listItemSelected.value = true
        selectedIndex.value = index
    }


    fun startCountdown() {
        _complete.value = false
        isCountingDown.value = true
        countdownJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
            isCountingDown.value = false
            _complete.value = true
        }
    }



    fun getGoalText(): String {
        return if (isCountingDown.value || complete.value) {
            "Goal"
        } else
            "Select a tag for your time slot:"

    }

    fun getText(): String {
        return "Study ${list[selectedIndex.value]} for ${hour.value} hr ${minutes.value} min"
    }

    fun resetCountdown() {
        countdownJob?.cancel()
        _remainingTime.value = 0
        isCountingDown.value = false
        _forwardStatus.value = false
        _timeSaved.value = false
        listItemSelected.value = false
        _complete.value=false
    }

    init {
        savedStateHandle.get<Int>("remainingTime")?.let {
            _remainingTime.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle["remainingTime"] = _remainingTime.value
    }

    fun convertToSeconds(hours: Int, minutes: Int, seconds: Int) {
        this.hour.value = hours
        this.minutes.value = minutes
        _timeSaved.value = true
        _remainingTime.value = hours * 3600 + minutes * 60 + seconds
    }
}
