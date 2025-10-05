package com.example.releaseflow.personal.domain.usecase.task

import com.example.releaseflow.core.domain.model.Task
import com.example.releaseflow.core.domain.model.TaskStatus
import com.example.releaseflow.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first

class ListTasksUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(projectId: Long): List<Task> = repo.getTasksByProject(projectId).first()
}

class GetTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: Long): Task? = repo.getTaskById(taskId).first()
}

class UpsertTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.updateTask(task)
}

class DeleteTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: Long) = repo.deleteTask(Task(id = taskId, projectId = 0, title = "", description = "", phase = com.example.releaseflow.core.domain.model.TaskPhase.PRE_PRODUCTION, priority = com.example.releaseflow.core.domain.model.TaskPriority.MEDIUM, status = com.example.releaseflow.core.domain.model.TaskStatus.PENDING, dueDate = java.time.LocalDate.now(), createdAt = 0, updatedAt = 0))
}

class UpdateTaskStatusUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: Long, status: TaskStatus) = repo.updateTaskStatus(taskId, status)
}

class UpsertSubTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(subTask: Task) = repo.updateTask(subTask)
}

class DeleteSubTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(subTaskId: Long) = repo.deleteTask(Task(id = subTaskId, projectId = 0, title = "", description = "", phase = com.example.releaseflow.core.domain.model.TaskPhase.PRE_PRODUCTION, priority = com.example.releaseflow.core.domain.model.TaskPriority.MEDIUM, status = com.example.releaseflow.core.domain.model.TaskStatus.PENDING, dueDate = java.time.LocalDate.now(), createdAt = 0, updatedAt = 0))
}

