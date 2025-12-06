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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.domain.model.Task

@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val state by viewModel.uiState.collectAsState()

    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        Text(text = "TODO LIST")

        Spacer(modifier = Modifier.height(16.dp))

        // БЛОК ДОБАВЛЕНИЯ ЗАДАЧИ
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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

        // ДАЛЬШЕ СОСТОЯНИЕ
        when {
            state.isLoading -> {
                Text(text = "Загрузка...")
            }

            state.error != null -> {
                Text(text = "Ошибка: ${state.error}")
            }

            else -> {
                state.tasks.forEach { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = if (task.isDone) "+ ${task.name}" else task.name)
                Text(text = task.description)
            }

            IconButton(onClick = { onDelete() }) {
                Text("X")
            }
        }
    }

}
