package com.example.releaseflow.core.domain.usecase.task

import com.example.releaseflow.core.domain.model.Task
import com.example.releaseflow.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksByProjectUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(projectId: Long): Flow<List<Task>> {
        return taskRepository.getTasksByProject(projectId)
    }
}
