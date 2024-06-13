import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import page.SelectPage
import page.ViewPage
import java.io.File

sealed class Page(val route: String) {
	data object Select : Page("select")
	data object View : Page("view")
}

@Composable
@Preview
fun App(window: ComposeWindow) {
	val navController = rememberNavController()
	var selectedDir by remember { mutableStateOf<File?>(null) }

	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Page.Select.route,
	) {
		composable(
			route = Page.Select.route,
		) {
			SelectPage(
				window = window,
				onDirectorySelected = { dir ->
					selectedDir = dir
					navController.navigate(Page.View.route)
				}
			)
		}

		composable(
			route = Page.View.route,
		) {
			fun back() {
				selectedDir = null
				navController.popBackStack(Page.Select.route, false)
			}

			val dir = selectedDir
			if (dir != null && dir.isDirectory) {
				ViewPage(
					groups = emptySet(),
					onBack = { back() },
				)
			} else {
				// TODO: alert!
				back()
			}
		}
	}
}

fun main() = application {
	val windowState = rememberWindowState(
		placement = WindowPlacement.Maximized,
		position = WindowPosition.PlatformDefault,
	)

	Window(
		onCloseRequest = ::exitApplication,
		state = windowState,
		title = "A-Shot",
		icon = painterResource("icons/icon.png")
	) {
		App(window = window)
	}
}
