package id.pasukanlangit.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.pasukanlangit.core.domain.usecase.TourismUseCase
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(tourismUseCase: TourismUseCase) : ViewModel(){
    val tourism = tourismUseCase.getAllTourism().asLiveData()
}