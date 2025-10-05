package com.example.releaseflow.personal.data.templates

import com.example.releaseflow.personal.data.local.entity.TaskCategory

data class TaskTemplate(
    val title: String,
    val dueDaysOffset: Int, // Days before release date (negative = before, positive = after)
    val priority: Int, // 1=HIGH, 2=MEDIUM, 3=LOW
    val category: TaskCategory,
    val description: String? = null
)
