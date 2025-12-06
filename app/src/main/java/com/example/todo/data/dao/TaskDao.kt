package com.example.todo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(task: TaskEntity)

    @Query("UPDATE tasks SET isDone = NOT isDone where id=:id")
    suspend fun toggleTask(id: Long)

    @Query("DELETE FROM tasks WHERE id=:id")
    suspend fun deleteTask(id: Long)
}