package database

import java.sql.Blob

data class ShotState(
    val photos: List<Shot> = emptyList(),
    val name: String = "",
    val image: Blob =,
    val isAddingPhoto: Boolean = false
)