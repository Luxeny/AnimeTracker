package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnimeDao {
    @Query("SELECT * FROM user_watchlist ORDER BY lastUpdated DESC")
    fun getWatchlistFlow(): Flow<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_watchlist WHERE id = :id LIMIT 1")
    fun getWatchlistAnimeFlow(id: Int): Flow<UserAnimeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userAnime: UserAnimeEntity)

    @Query("DELETE FROM user_watchlist WHERE id = :id")
    suspend fun deleteById(id: Int)
}
