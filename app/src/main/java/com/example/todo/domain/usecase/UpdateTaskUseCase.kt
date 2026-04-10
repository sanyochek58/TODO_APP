package com.example.todo.domain.usecase

import com.example.todo.domain.repository.TaskRepository

class UpdateTaskUseCase(private val repository: TaskRepository) {

    suspend fun updateTask(id: Long, name: String, description: String) {
        repository.updateTask(id, name, description)
    }
}
