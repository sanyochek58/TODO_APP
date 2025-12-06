package com.example.todo.domain.model

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val isDone: Boolean
)