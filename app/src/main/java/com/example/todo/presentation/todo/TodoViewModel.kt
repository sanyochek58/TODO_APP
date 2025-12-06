package com.example.todo.presentation.todo

import android.app.ActivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.usecase.AddTaskUseCase
import com.example.todo.domain.usecase.DeleteTaskUseCase
import com.example.todo.domain.usecase.GetTasksUseCase
import com.example.todo.domain.usecase.ToggleTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val toggleTasksUseCase: ToggleTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState(isLoading = true))
    val uiState: StateFlow<TodoUiState> = _uiState

    init {
        initTasks()
    }

    fun initTasks() {
        viewModelScope.launch {
            getTasksUseCase.getTasks()
                .catch { e ->
                    _uiState.value = TodoUiState(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { tasks ->
                    _uiState.value = TodoUiState(
                        isLoading = false,
                        tasks = tasks
                    )
                }
        }
    }

    fun addTask(name: String, description: String){
        viewModelScope.launch {
            addTaskUseCase.addTask(name,description)
        }
    }

    fun toggleTask(id: Long){
        viewModelScope.launch {
            toggleTasksUseCase.toggleTask(id)
        }
    }

    fun deleteTask(id: Long){
        viewModelScope.launch {
            deleteTaskUseCase.deleteTask(id)
        }
    }
}
