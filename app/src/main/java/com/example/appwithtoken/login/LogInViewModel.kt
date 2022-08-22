package com.example.appwithtoken.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.appwithtoken.Resource
import com.example.appwithtoken.RetrofitClient
import com.example.appwithtoken.User
import com.example.appwithtoken.UserPreference
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    private val _logInState = MutableSharedFlow<Resource<LogIn>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logInState get() = _logInState.asSharedFlow()

    fun logIn(user: User) {
        viewModelScope.launch {
            logInResponse(user.email, user.pass).collect() {
                _logInState.tryEmit(it)
            }
        }
    }

    private fun logInResponse(email: String, password: String) = flow {
        val model = LogInModel(email, password)

        val response = RetrofitClient.authService.logIn(model)

        if (response.isSuccessful) {
            val body = response.body()
            emit(Resource.Success(body!!))

            UserPreference.saveString(body.token ?: "")
        } else {
            val errorBody = response.errorBody()
            emit(Resource.Error(errorBody.toString()))
        }
    }

    fun hasSession() = UserPreference.getString(UserPreference.TOKEN)?.isNotEmpty() ?: false
}