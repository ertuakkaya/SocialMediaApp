package com.example.socialmediaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatSelectViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel(){


    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    /**
     *  Get the list of users from Firestore
     */
    fun getUsers() {
        viewModelScope.launch {
           firestoreRepository.fetchUsers().collect{users ->
                _users.value = users
            }
        }


    }

}