package com.example.todo.data.repository

import com.example.todo.data.dao.TaskDao
import com.example.todo.data.local.entity.TaskEntity
import com.example.todo.data.mapper.toDomain
import com.example.todo.domain.model.Task
import com.example.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val taskDao: TaskDao): TaskRepository{

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks().map{list -> list.map{it.toDomain()}}
    }

    override suspend fun addTask(name: String, description: String) {
        val entity = TaskEntity(name = name, description = description)
        taskDao.addTask(entity)
    }

    override suspend fun toggleTask(id: Long) {
        taskDao.toggleTask(id)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }
}