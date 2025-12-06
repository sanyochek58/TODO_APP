package com.example.todo.domain.usecase

import com.example.todo.domain.repository.TaskRepository

class ToggleTasksUseCase(private val repository: TaskRepository) {

   suspend fun toggleTask(id: Long){
        return repository.toggleTask(id)
    }
}