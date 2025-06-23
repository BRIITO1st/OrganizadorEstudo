package com.example.organizadorestudo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizadorestudo.data.StudyDatabase
import com.example.organizadorestudo.data.Task
import com.example.organizadorestudo.data.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao: TaskDao
    val allTasks: Flow<List<Task>>

    init {
        val database = StudyDatabase.getDatabase(application)
        taskDao = database.taskDao()
        allTasks = taskDao.getAllTasks()
    }

    fun addTask(title: String, subject: String) = viewModelScope.launch {
        taskDao.insert(Task(title = title, subject = subject))
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }
}
