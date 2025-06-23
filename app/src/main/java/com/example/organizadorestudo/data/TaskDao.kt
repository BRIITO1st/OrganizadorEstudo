package com.example.organizadorestudo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    // READ
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    // UPDATE
    @Update
    suspend fun update(task: Task)

    // DELETE
    @Delete
    suspend fun delete(task: Task)
}
