package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateClassRoomViewModel: ViewModel() {
    var state = mutableStateOf(CreateClassRoomState())
        private set
}