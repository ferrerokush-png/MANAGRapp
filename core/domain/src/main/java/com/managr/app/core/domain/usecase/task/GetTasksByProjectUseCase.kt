package com.managr.app.core.domain.usecase.task

import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksByProjectUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(projectId: Long): Flow<List<Task>> {
        return taskRepository.getTasksByProject(projectId)
    }
}
