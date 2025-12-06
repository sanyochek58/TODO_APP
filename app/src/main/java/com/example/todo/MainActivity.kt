package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.todo.data.db.AppDatabase
import com.example.todo.data.repository.TaskRepositoryImpl
import com.example.todo.domain.usecase.AddTaskUseCase
import com.example.todo.domain.usecase.DeleteTaskUseCase
import com.example.todo.domain.usecase.GetTasksUseCase
import com.example.todo.domain.usecase.ToggleTasksUseCase
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

        val getTasksUseCase = GetTasksUseCase(repository)
        val addTaskUseCase = AddTaskUseCase(repository)
        val toggleTaskUseCase = ToggleTasksUseCase(repository)
        val deleteTaskUseCase = DeleteTaskUseCase(repository)

        val viewModel = TodoViewModel(
            getTasksUseCase,
            addTaskUseCase,
            toggleTaskUseCase,
            deleteTaskUseCase
        )

        setContent {
            TODOTheme {
                TodoScreen(viewModel = viewModel)
            }
        }
    }
}
