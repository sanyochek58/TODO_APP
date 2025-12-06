package com.example.todo.domain.usecase

import com.example.todo.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {

   suspend fun deleteTask(id: Long){
        return repository.deleteTask(id);
    }
}