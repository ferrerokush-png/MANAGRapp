package com.example.releaseflow.personal.domain.repository

import com.example.releaseflow.personal.domain.model.SubTask
import com.example.releaseflow.personal.domain.model.Task
import com.example.releaseflow.personal.domain.model.TaskStatus

interface TaskRepository {
    suspend fun listTasks(projectId: String): List<Task>
    suspend fun getTask(taskId: String): Task?
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(taskId: String)

    // Subtasks
    suspend fun upsertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(subTaskId: String)

    // Convenience operations
    suspend fun updateTaskStatus(taskId: String, status: TaskStatus)
}

