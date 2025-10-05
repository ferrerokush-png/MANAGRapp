package com.example.releaseflow.personal.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.entity.Contact
import com.example.releaseflow.personal.data.local.entity.ContactType
import com.example.releaseflow.personal.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

sealed class ContactUiState {
    object Loading : ContactUiState()
    data class Success(val contacts: List<Contact>) : ContactUiState()
    data class Error(val message: String) : ContactUiState()
}

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContactUiState>(ContactUiState.Loading)
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    private val _selectedContactType = MutableStateFlow<ContactType?>(null)
    val selectedContactType: StateFlow<ContactType?> = _selectedContactType.asStateFlow()

    init {
        loadAllContacts()
    }

    fun loadAllContacts() {
        viewModelScope.launch {
            try {
                contactRepository.getAllContacts()
                    .catch { e ->
                        _uiState.value = ContactUiState.Error(e.message ?: "Unknown error")
                    }
                    .collect { contacts ->
                        _uiState.value = ContactUiState.Success(contacts)
                    }
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to load contacts")
            }
        }
    }

    fun filterByType(type: ContactType?) {
        _selectedContactType.value = type
        if (type == null) {
            loadAllContacts()
        } else {
            viewModelScope.launch {
                try {
                    contactRepository.getContactsByType(type.name)
                        .catch { e ->
                            _uiState.value = ContactUiState.Error(e.message ?: "Unknown error")
                        }
                        .collect { contacts ->
                            _uiState.value = ContactUiState.Success(contacts)
                        }
                } catch (e: Exception) {
                    _uiState.value = ContactUiState.Error(e.message ?: "Failed to filter contacts")
                }
            }
        }
    }

    fun insertContact(
        name: String,
        type: ContactType,
        email: String? = null,
        platform: String? = null,
        genreFocus: String? = null,
        submissionUrl: String? = null,
        notes: String? = null
    ) {
        viewModelScope.launch {
            try {
                contactRepository.insertContact(
                    Contact(
                        name = name,
                        type = type,
                        email = email,
                        platform = platform,
                        genreFocus = genreFocus,
                        submissionUrl = submissionUrl,
                        notes = notes,
                        createdAt = Date()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to insert contact")
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactRepository.updateContact(contact)
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to update contact")
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactRepository.deleteContact(contact)
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to delete contact")
            }
        }
    }

    fun updateLastContact(contact: Contact, date: Date) {
        viewModelScope.launch {
            try {
                contactRepository.updateContact(contact.copy(lastContact = date))
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to update last contact")
            }
        }
    }

    fun updateSuccessRate(contact: Contact, rate: Float) {
        viewModelScope.launch {
            try {
                contactRepository.updateContact(contact.copy(successRate = rate))
            } catch (e: Exception) {
                _uiState.value = ContactUiState.Error(e.message ?: "Failed to update success rate")
            }
        }
    }
}
