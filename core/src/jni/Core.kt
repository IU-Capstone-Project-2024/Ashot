package core.src.jni

object Core {
	fun load(path: String) {
		System.load(path)
	}

	external fun hello(): Int
}