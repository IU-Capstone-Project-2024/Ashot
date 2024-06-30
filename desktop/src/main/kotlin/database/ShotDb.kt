package database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity
data class Photo(
	@PrimaryKey(autoGenerate = true) val id: Int,
	val name: String,
	val image: Blob
)

