package com.example.releaseflow.core.domain.usecase.task

import com.example.releaseflow.core.domain.model.Task
import com.example.releaseflow.core.domain.repository.TaskRepository

class UpdateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        return try {
            task.validate().getOrElse { return Result.failure(it) }
            val updated = task.copy(updatedAt = System.currentTimeMillis())
            taskRepository.updateTask(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
