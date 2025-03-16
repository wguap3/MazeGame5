package com.example.mazegame5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

class GameTimeViewModel : ViewModel() {
    private val _level1Time = MutableLiveData<Long>()
    val level1Time: LiveData<Long> = _level1Time

    private val _level2Time = MutableLiveData<Long>()
    val level2Time: LiveData<Long> = _level2Time

    private val _level3Time = MutableLiveData<Long>()
    val level3Time: LiveData<Long> = _level3Time

    fun setLevel1Time(time: Long) {
        _level1Time.value = time
    }

    fun setLevel2Time(time: Long) {
        _level2Time.value = time
    }

    fun setLevel3Time(time: Long) {
        _level3Time.value = time
    }
}
