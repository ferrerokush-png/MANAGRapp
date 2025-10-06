package com.managr.app.feature.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

sealed class HubUiState {
    object Loading : HubUiState()
    data class Success(val contacts: List<ContactItem>) : HubUiState()
    data class Error(val message: String) : HubUiState()
}

@HiltViewModel
class HubViewModel @Inject constructor(
    // NOTE: Inject contact repository when available
) : ViewModel() {

    private val _selectedType = MutableStateFlow(ContactType.CURATOR)
    val selectedType: StateFlow<ContactType> = _selectedType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<HubUiState>(HubUiState.Loading)
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            // Mock data for now
            _uiState.value = HubUiState.Success(emptyList())
        }
    }

    fun selectType(type: ContactType) {
        _selectedType.value = type
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addContact(
        name: String,
        type: ContactType,
        email: String?,
        platform: String?,
        genreFocus: String?,
        submissionUrl: String?
    ) {
        viewModelScope.launch {
            // NOTE: Implement with repository
        }
    }

    fun refreshContacts() {
        loadContacts()
    }
}

enum class ContactType {
    CURATOR, BLOGGER, RADIO, LABEL
}

data class ContactItem(
    val id: Long,
    val name: String,
    val type: ContactType,
    val email: String?,
    val platform: String?,
    val genreFocus: String?,
    val submissionUrl: String?,
    val lastContact: Date?,
    val successRate: Float?
)
