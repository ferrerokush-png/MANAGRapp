package com.example.releaseflow.core.domain.usecase.task

import com.example.releaseflow.core.domain.repository.TaskRepository
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
