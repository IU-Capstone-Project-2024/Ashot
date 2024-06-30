package database

import androidx.room.RoomDatabase


@Database (
	entities = [Photo::class],
	version = 1
)
abstract class ShotDatabase: RoomDatabase() {
	abstract val dao: ImageDao
}