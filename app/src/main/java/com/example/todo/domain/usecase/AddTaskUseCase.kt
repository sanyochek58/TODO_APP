package com.example.todo.domain.usecase

import com.example.todo.domain.repository.TaskRepository

class AddTaskUseCase(private val repository: TaskRepository) {

   suspend fun addTask(name: String, description: String){
        return repository.addTask(name, description)
    }
}