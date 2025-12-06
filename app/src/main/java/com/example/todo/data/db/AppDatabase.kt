package com.example.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.dao.TaskDao
import com.example.todo.data.local.entity.TaskEntity
import com.example.todo.domain.model.Task

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}