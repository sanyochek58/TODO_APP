package com.example.todo.domain.repository

import com.example.todo.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun addTask(name: String, description: String)
    suspend fun toggleTask(id: Long)
    suspend fun deleteTask(id: Long)
}