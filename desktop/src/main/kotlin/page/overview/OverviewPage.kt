package page.overview

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OverviewPage(
	onGroupsClick: () -> Unit,
	onTailpondClick: () -> Unit,
) {
	Row(
		modifier = Modifier.fillMaxSize(),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Button(onClick = onGroupsClick) {
			Text("Good")
		}
		Spacer(modifier = Modifier.width(8.dp))
		Button(onClick = onTailpondClick) {
			Text("Bad")
		}
	}
}
