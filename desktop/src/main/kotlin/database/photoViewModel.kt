package database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


//todo: fix errors
class photoViewModel(
	private val dao: ImageDao
) : ViewModel() {
	private val _state = MutableStateFlow(PhotoState())
	private val _photos = dao.getImages() // todo: implement sort maybe

	val state = combine(_state, _photos) {state, photos ->
		state.copy(
			photos = photos
		)
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PhotoState())

	fun onEvent(event: PhotoEvent) {
		when(event) {
			is PhotoEvent.DeletePhoto -> {
				viewModelScope.launch {
					dao.deleteImage(event.photo)
				}
			}
			is PhotoEvent.SetName -> {
				_state.update {
					it.copy(
						name = event.name
					)
				}
			}
			is PhotoEvent.SetPhoto -> {
				_state.update {
					it.copy(
						image = event.image
					)
				}
			}
			PhotoEvent.showImage -> {
				_state.update {
					it.copy(
						isAddingPhoto = true
					)
				}
			}
			PhotoEvent.SavePhoto -> {
				val name = state.value.name
				val image = state.value.image

				if (name.isBlank() || image.length().equals(0)) {
					return
				}

				val photo = Photo(
					name = name,
					image = image
				)
				viewModelScope.launch {
					dao.incertImage(photo)
				}
				_state.update {
					it.copy(
						isAddingPhoto = false,
						name = "",
						image = ""
					)
				}
			}
		}
	}
}