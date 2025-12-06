package com.example.todo.domain.usecase

import com.example.todo.domain.model.Task
import com.example.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: TaskRepository) {

    fun getTasks(): Flow<List<Task>> {
        return repository.getTasks()
    }
}