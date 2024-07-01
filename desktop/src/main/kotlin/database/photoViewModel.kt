package database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


//todo: fix errors
class photoViewModel(
    private val dao: ShotDao
) : ViewModel() {
    private val _state = MutableStateFlow(ShotState())
    private val _photos = dao.getShots() // todo: implement sort maybe

    val state = combine(_state, _photos) { state, photos ->
        state.copy(
            photos = photos
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShotState())

    fun onEvent(event: PhotoEvent) {
        when (event) {
            is PhotoEvent.DeleteShot -> {
                viewModelScope.launch {
                    dao.deleteImage(event.shot)
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

            PhotoEvent.showShot -> {
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

                val photo = Shot(
                    filepath = name,
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