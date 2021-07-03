package id.pasukanlangit.maps.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.pasukanlangit.core.domain.usecase.TourismUseCase
import id.pasukanlangit.maps.MapsViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val tourismUseCase: TourismUseCase): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(tourismUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}