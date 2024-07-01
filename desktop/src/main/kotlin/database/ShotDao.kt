package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShotDao {

    @Upsert
    fun insertShot(shot: Shot)

    @Delete
    suspend fun deleteImage(shot: Shot)

    @Query("SELECT * FROM Shot")
    fun getShots(): Flow<List<Shot>>

    @Query("SELECT * FROM Shot ORDER BY filepath ASC")
    fun getShotsOrderedByPath(): Flow<List<Shot>>
}