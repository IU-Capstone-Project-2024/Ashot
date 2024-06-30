import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

// this class may be used to delete all other
class DatabaseHelper {
	private val connection: Connection

	init {
		val url = "jdbc:sqlite:sample.db"
		connection = DriverManager.getConnection(url)
		createTable()
	}

	private fun createTable() {
		val statement = connection.createStatement()
		statement.execute("CREATE TABLE IF NOT EXISTS photos (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT NOT NULL, image BLOB)")
	}

	fun insertPerson(name: String, image: ByteArray?) {
		val sql = "INSERT INTO photos (name, image) VALUES(?, ?)"
		val pstmt = connection.prepareStatement(sql)
		pstmt.setString(1, name)
		pstmt.setBytes(2, image)
		pstmt.executeUpdate()
	}

	fun getAllPersons(): List<Pair<String, ByteArray?>> {
		val sql = "SELECT name, image FROM photos"
		val statement = connection.createStatement()
		val resultSet: ResultSet = statement.executeQuery(sql)

		val persons = mutableListOf<Pair<String, ByteArray?>>()
		while (resultSet.next()) {
			val name = resultSet.getString("name")
			val image = resultSet.getBytes("image")
			persons.add(name to image)
		}
		return persons
	}
}
