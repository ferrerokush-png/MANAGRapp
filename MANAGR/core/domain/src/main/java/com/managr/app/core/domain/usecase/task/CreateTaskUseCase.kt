package com.managr.app.core.domain.usecase.task

import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.repository.TaskRepository
import com.managr.app.core.domain.util.ValidationRules
import javax.inject.Inject

/**
 * Use case for creating a new task
 */
class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Create a new task
     * 
     * @param task Task to create
     * @return Result with task ID or error
     */
    suspend operator fun invoke(task: Task): Result<Long> {
        return try {
            // Validate task
            task.validate().getOrElse { return Result.failure(it) }
            
            // Additional validation
            if (!ValidationRules.isValidTaskTitle(task.title)) {
                return Result.failure(IllegalArgumentException("Invalid task title"))
            }
            
            // Insert task
            val taskId = taskRepository.insertTask(task)
            Result.success(taskId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Create multiple tasks
     */
    suspend fun createTasks(tasks: List<Task>): Result<List<Long>> {
        return try {
            // Validate all tasks
            tasks.forEach { task ->
                task.validate().getOrElse { return Result.failure(it) }
            }
            
            // Insert tasks
            val taskIds = taskRepository.insertTasks(tasks)
            Result.success(taskIds)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
