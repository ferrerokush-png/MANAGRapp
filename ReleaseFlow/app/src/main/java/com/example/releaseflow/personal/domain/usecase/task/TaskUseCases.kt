package com.example.releaseflow.personal.domain.usecase.task

import com.example.releaseflow.personal.domain.model.SubTask
import com.example.releaseflow.personal.domain.model.Task
import com.example.releaseflow.personal.domain.model.TaskStatus
import com.example.releaseflow.personal.domain.repository.TaskRepository

class ListTasksUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(projectId: String): List<Task> = repo.listTasks(projectId)
}

class GetTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: String): Task? = repo.getTask(taskId)
}

class UpsertTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task) = repo.upsertTask(task)
}

class DeleteTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repo.deleteTask(taskId)
}

class UpdateTaskStatusUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(taskId: String, status: TaskStatus) = repo.updateTaskStatus(taskId, status)
}

class UpsertSubTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(subTask: SubTask) = repo.upsertSubTask(subTask)
}

class DeleteSubTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(subTaskId: String) = repo.deleteSubTask(subTaskId)
}

