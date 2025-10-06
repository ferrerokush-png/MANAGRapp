package com.managr.app.core.domain.usecase.task

import com.managr.app.core.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskCompletionUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Long): Result<Unit> {
        return try {
            taskRepository.toggleTaskCompletion(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
