package com.uni.fsm.presentation.navigation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uni.fsm.data.local.SessionManager
import com.uni.fsm.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(context: Context) : ViewModel() {
    private val _session = MutableStateFlow<User?>(null)
    val session: StateFlow<User?> = _session

    init {
        viewModelScope.launch {
            _session.value = SessionManager.getUserSession(context)
        }
    }
}



class SessionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SessionViewModel(context.applicationContext) as T
    }
}
