package database

import java.sql.Blob

sealed interface PhotoEvent {
    object SavePhoto : PhotoEvent
    data class SetName(val name: String) : PhotoEvent
    data class SetPhoto(val image: Blob) : PhotoEvent

    object showShot : PhotoEvent

    // data class SortPhotos(val sortType)
    data class DeleteShot(val shot: Shot) : PhotoEvent
}