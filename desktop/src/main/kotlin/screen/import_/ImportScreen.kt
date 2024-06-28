package screen.import_

import androidx.compose.runtime.*
import core.src.jni.LoadingPipeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import screen.import_.component.Container
import screen.import_.component.Failure
import screen.import_.component.Loading
import screen.import_.component.SelectDir
import shot.Shot
import shot.ShotCollection
import java.io.File

@Composable
fun ImportScreen(
	onImported: (File, ShotCollection) -> Unit,
) {
	val scope = rememberCoroutineScope()
	val loader = remember { LoadingPipeline() }
	val model = remember { ImportModel(onImported) }
	val state by model.stateFlow.collectAsState()

	Container(state) { current ->
		when (current) {
			ImportState.SelectDir -> {
				SelectDir { dir ->
					model.import(dir)
				}
			}
			is ImportState.Loading -> {
				LaunchedEffect(current.dir) {
					scope.launch(Dispatchers.IO) {
						val result = loader.load(current.dir.absolutePath)
						if (!result) {
							println("Too many clicks")
						}
						loader.flow().collect { (path, score) ->
							val shot = Shot(File(path))
							if (score < 0.1f) {
								model.adding(shot, false)
							} else {
								model.adding(shot, true)
							}
						}
					}
				}

				Loading(
					dirName = current.dir.name,
					progress = current.progress,
					onCancel = {
						model.reset()
					}
				)
			}
			is ImportState.Failure -> {
				Failure(
					reason = current.reason,
					onOk = {
						model.reset()
					}
				)
			}
		}
	}
}
