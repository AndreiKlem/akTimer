package ru.aklem.aktimer.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChartDao {

    @Query("SELECT * FROM chart_database ORDER BY title ASC")
    fun getAll(): LiveData<List<Chart>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chart: Chart)

    @Update
    suspend fun update(chart: Chart)

    @Delete
    suspend fun delete(chart: Chart)

    @Query("DELETE FROM chart_database")
    suspend fun deleteAllCharts()
}