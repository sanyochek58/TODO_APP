package com.example.todo.presentation.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo.domain.model.Task

private val CompletedTaskColor = Color(0xFFA5D6A7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val state by viewModel.uiState.collectAsState()

    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODO LIST") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    Text(
                        text = "Цвет завершенных",
                        modifier = Modifier.padding(end = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Switch(
                        checked = state.completedColorEnabled,
                        onCheckedChange = { viewModel.setCompletedColorEnabled(it) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // БЛОК ДОБАВЛЕНИЯ ЗАДАЧИ
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Название задачи") },
                    placeholder = { Text("Например: Купить продукты") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Описание") },
                    placeholder = { Text("Например: Молоко, яйца, хлеб") },
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (newName.isNotBlank() && newDescription.isNotBlank()) {
                                viewModel.addTask(newName, newDescription)
                                newName = ""
                                newDescription = ""
                            }
                        }
                    ) {
                        Text(text = "Добавить")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // СПИСОК ЗАДАЧ
            when {
                state.isLoading -> {
                    Text(text = "Загрузка...")
                }

                state.error != null -> {
                    Text(text = "Ошибка: ${state.error}")
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.tasks, key = { it.id }) { task ->
                            TaskItem(
                                task = task,
                                completedColorEnabled = state.completedColorEnabled,
                                onToggle = { viewModel.toggleTask(task.id) },
                                onEdit = { viewModel.startEditing(task) },
                                onDelete = { viewModel.deleteTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // ДИАЛОГ РЕДАКТИРОВАНИЯ
    state.editingTask?.let { task ->
        EditTaskDialog(
            task = task,
            onConfirm = { name, description -> viewModel.saveEdit(name, description) },
            onDismiss = { viewModel.cancelEditing() }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    completedColorEnabled: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (task.isDone && completedColorEnabled)
                CompletedTaskColor
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (task.isDone) "✓ ${task.name}" else task.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = { onEdit() }) {
                Text("✎")
            }

            IconButton(onClick = { onDelete() }) {
                Text("✕")
            }
        }
    }
}

@Composable
fun EditTaskDialog(
    task: Task,
    onConfirm: (name: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать задачу") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Название") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Описание") },
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && description.isNotBlank()) {
                        onConfirm(name, description)
                    }
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
