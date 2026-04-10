package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.todo.data.datastore.SettingsDataStore
import com.example.todo.data.db.AppDatabase
import com.example.todo.data.repository.TaskRepositoryImpl
import com.example.todo.domain.usecase.AddTaskUseCase
import com.example.todo.domain.usecase.DeleteTaskUseCase
import com.example.todo.domain.usecase.GetTasksUseCase
import com.example.todo.domain.usecase.ImportTasksUseCase
import com.example.todo.domain.usecase.ToggleTasksUseCase
import com.example.todo.domain.usecase.UpdateTaskUseCase
import com.example.todo.presentation.todo.TodoScreen
import com.example.todo.presentation.todo.TodoViewModel
import com.example.todo.ui.theme.TODOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "todo.db"
        ).build()

        val taskDao = db.taskDao()
        val repository = TaskRepositoryImpl(taskDao)

        val settingsDataStore = SettingsDataStore(applicationContext)

        val tasksJson = assets.open("tasks.json").bufferedReader().use { it.readText() }

        val viewModel = TodoViewModel(
            getTasksUseCase = GetTasksUseCase(repository),
            addTaskUseCase = AddTaskUseCase(repository),
            toggleTasksUseCase = ToggleTasksUseCase(repository),
            deleteTaskUseCase = DeleteTaskUseCase(repository),
            updateTaskUseCase = UpdateTaskUseCase(repository),
            importTasksUseCase = ImportTasksUseCase(repository, settingsDataStore),
            settingsDataStore = settingsDataStore,
            tasksJson = tasksJson
        )

        setContent {
            TODOTheme {
                TodoScreen(viewModel = viewModel)
            }
        }
    }
}
