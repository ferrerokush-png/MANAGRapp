package com.managr.app.core.domain.usecase.task

import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.repository.TaskRepository

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
