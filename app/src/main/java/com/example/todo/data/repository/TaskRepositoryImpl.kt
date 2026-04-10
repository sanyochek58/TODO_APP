package com.example.todo.data.repository

import com.example.todo.data.dao.TaskDao
import com.example.todo.data.local.entity.TaskEntity
import com.example.todo.data.mapper.toDomain
import com.example.todo.domain.model.Task
import com.example.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addTask(name: String, description: String) {
        val entity = TaskEntity(name = name, description = description)
        taskDao.addTask(entity)
    }

    override suspend fun updateTask(id: Long, name: String, description: String) {
        taskDao.updateTask(id, name, description)
    }

    override suspend fun toggleTask(id: Long) {
        taskDao.toggleTask(id)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun importTasksFromJson(jsonString: String) {
        val jsonArray = JSONArray(jsonString)
        val entities = (0 until jsonArray.length()).map { i ->
            val obj = jsonArray.getJSONObject(i)
            TaskEntity(
                name = obj.getString("name"),
                description = obj.getString("description")
            )
        }
        taskDao.insertAll(entities)
    }
}
