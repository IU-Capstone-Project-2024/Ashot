package database

import java.sql.Blob

data class PhotoState {
	val photos: List<Photo> = emptyList();
	val name: String = "";
	val image: Blob = ;
	val isAddingPhoto: Boolean = false;

}