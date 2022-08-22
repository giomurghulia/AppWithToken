package com.example.appwithtoken.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appwithtoken.Resource
import com.example.appwithtoken.RetrofitClient
import com.example.appwithtoken.User
import com.example.appwithtoken.login.LogIn
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<Register>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val registerState get() = _registerState.asSharedFlow()

    private val _logInState = MutableSharedFlow<Resource<LogIn>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logInState get() = _logInState.asSharedFlow()

    fun register(user: User) {
        viewModelScope.launch {
            val model = RegisterModel(user.email, user.pass)

            val response = RetrofitClient.authService.register(model)

            if (response.isSuccessful) {
                val body = response.body()
                _registerState.tryEmit(Resource.Success(body!!))
            } else {
                val errorBody = response.errorBody()
                _registerState.tryEmit(Resource.Error(errorBody?.toString()!!))
            }
        }
    }
}