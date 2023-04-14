package com.bichngoc.demngayapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bichngoc.demngayapp.room.UserDB

class MViewModel : ViewModel() {
    private val _message = MutableLiveData<UserDB>(null)//init = rỗng

    fun sendData(user: UserDB) {
        _message.value = user
    }

    val message: LiveData<UserDB> = _message //LiveData chỉ đọc được
}