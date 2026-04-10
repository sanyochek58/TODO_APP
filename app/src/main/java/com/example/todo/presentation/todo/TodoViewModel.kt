package com.example.todo.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.datastore.SettingsDataStore
import com.example.todo.domain.model.Task
import com.example.todo.domain.usecase.AddTaskUseCase
import com.example.todo.domain.usecase.DeleteTaskUseCase
import com.example.todo.domain.usecase.GetTasksUseCase
import com.example.todo.domain.usecase.ImportTasksUseCase
import com.example.todo.domain.usecase.ToggleTasksUseCase
import com.example.todo.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val toggleTasksUseCase: ToggleTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val settingsDataStore: SettingsDataStore,
    private val tasksJson: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState(isLoading = true))
    val uiState: StateFlow<TodoUiState> = _uiState

    private val _editingTask = MutableStateFlow<Task?>(null)

    init {
        viewModelScope.launch {
            importTasksUseCase.importIfNeeded(tasksJson)
        }

        viewModelScope.launch {
            combine(
                getTasksUseCase.getTasks()
                    .catch { e ->
                        _uiState.value = TodoUiState(isLoading = false, error = e.message)
                    },
                settingsDataStore.completedColorEnabled,
                _editingTask
            ) { tasks, colorEnabled, editingTask ->
                TodoUiState(
                    isLoading = false,
                    tasks = tasks,
                    completedColorEnabled = colorEnabled,
                    editingTask = editingTask
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun addTask(name: String, description: String) {
        viewModelScope.launch {
            addTaskUseCase.addTask(name, description)
        }
    }

    fun startEditing(task: Task) {
        _editingTask.value = task
    }

    fun cancelEditing() {
        _editingTask.value = null
    }

    fun saveEdit(name: String, description: String) {
        val task = _editingTask.value ?: return
        viewModelScope.launch {
            updateTaskUseCase.updateTask(task.id, name, description)
            _editingTask.value = null
        }
    }

    fun toggleTask(id: Long) {
        viewModelScope.launch {
            toggleTasksUseCase.toggleTask(id)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            deleteTaskUseCase.deleteTask(id)
        }
    }

    fun setCompletedColorEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setCompletedColorEnabled(enabled)
        }
    }
}
