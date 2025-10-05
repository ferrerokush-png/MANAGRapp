package com.example.releaseflow.feature.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.core.domain.model.CRMContact
import com.example.releaseflow.core.domain.usecase.crm.ManageCRMContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionsViewModel @Inject constructor(
    private val manageCRMContactsUseCase: ManageCRMContactsUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _contacts = MutableStateFlow<List<CRMContact>>(emptyList())
    val contacts: StateFlow<List<CRMContact>> = _contacts.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            manageCRMContactsUseCase.getAllContacts().collect { _contacts.value = it }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
}
