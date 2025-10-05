package com.example.releaseflow.core.domain.usecase.task

import com.example.releaseflow.core.domain.model.Task
import com.example.releaseflow.core.domain.repository.TaskRepository

class ReorderTasksUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(tasks: List<Task>): Result<Unit> {
        return try {
            val reorderedTasks = tasks.mapIndexed { index, task ->
                task.copy(order = index, updatedAt = System.currentTimeMillis())
            }
            taskRepository.updateTaskOrder(reorderedTasks)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
