package database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity
data class Shot(
    // @PrimaryKey(autoGenerate = true) val id: Int,
    @PrimaryKey(autoGenerate = false) val filepath: String,
    val blur_score: Float,
    val embedding: Float,
    val thumbnail: Blob
)

