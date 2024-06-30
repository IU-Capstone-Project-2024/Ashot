package database

import androidx.room.Dao
import androidx.room.Upsert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

	@Upsert
	fun incertImage(photo: Photo)

	@Delete
	suspend fun deleteImage(photo: Photo)

	@Query("SELECT * FROM photos")
	fun getImages() : Flow<List<Photo>>
	@Query("SELECT * FROM photos ORDER BY name ASC")
	fun getImagesOrderedByName() : Flow<List<Photo>>
}