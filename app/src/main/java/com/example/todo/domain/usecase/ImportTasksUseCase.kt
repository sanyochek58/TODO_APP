package com.example.todo.domain.usecase

import com.example.todo.data.datastore.SettingsDataStore
import com.example.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first

class ImportTasksUseCase(
    private val repository: TaskRepository,
    private val settingsDataStore: SettingsDataStore
) {

    suspend fun importIfNeeded(jsonString: String) {
        val alreadyImported = settingsDataStore.jsonImported.first()
        if (!alreadyImported) {
            repository.importTasksFromJson(jsonString)
            settingsDataStore.setJsonImported(true)
        }
    }
}
