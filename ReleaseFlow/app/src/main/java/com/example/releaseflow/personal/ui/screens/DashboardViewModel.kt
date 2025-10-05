package com.example.releaseflow.personal.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.dao.AppDao
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val appDao: AppDao
) : ViewModel() {

    val projects: StateFlow<List<ReleaseProject>> =
        appDao.getAllProjects()
            .map { it.sortedBy { p -> p.releaseDate } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addProject(name: String, projectType: String, releaseDateMillis: Long) {
        if (name.isBlank()) return
        viewModelScope.launch {
            appDao.insertProject(
                ReleaseProject(
                    name = name.trim(),
                    projectType = projectType.trim(),
                    releaseDate = releaseDateMillis
                )
            )
        }
    }
}
