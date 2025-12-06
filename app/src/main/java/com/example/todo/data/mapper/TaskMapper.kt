package com.example.todo.data.mapper

import com.example.todo.data.local.entity.TaskEntity
import com.example.todo.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        name = name,
        description = description,
        isDone = isDone
    )
}

fun Task.toEntity(): TaskEntity{
     return TaskEntity(
        id = id,
        name = name,
        description = description,
        isDone = isDone
    )
}