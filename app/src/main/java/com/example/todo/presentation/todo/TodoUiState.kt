package com.example.todo.presentation.todo

import com.example.todo.domain.model.Task

data class TodoUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)
