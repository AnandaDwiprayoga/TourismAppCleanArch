package id.pasukanlangit.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.EntryPointAccessors
import id.pasukanlangit.core.data.Resource
import id.pasukanlangit.maps.databinding.ActivityMapsBinding
import id.pasukanlangit.maps.di.DaggerMapsComponent
import com.dicoding.tourismapp.di.MapsModuleDepedency
import id.pasukanlangit.maps.di.ViewModelFactory
import javax.inject.Inject

class MapsActivity : AppCompatActivity(R.layout.activity_maps) {

    @Inject
    lateinit var factory: ViewModelFactory
    private val binding: ActivityMapsBinding by viewBinding()
    private val viewModel: MapsViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMapsComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    MapsModuleDepedency::class.java
                )
            )
            .build()
            .inject(this)

        supportActionBar?.title = "Tourism Map"

        getTourismData()
    }

    private fun getTourismData() {
        viewModel.tourism.observe(this){ tourism ->
            if(tourism != null){
                binding.progressBar.isVisible = tourism is Resource.Loading
                when(tourism){
                    is Resource.Success -> {
                        binding.tvMaps.text = "This is map of ${tourism.data?.get(0)?.name}"
                    }
                    is Resource.Error -> {
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = tourism.message
                    }
                    else -> {}
                }
            }
        }
    }
}