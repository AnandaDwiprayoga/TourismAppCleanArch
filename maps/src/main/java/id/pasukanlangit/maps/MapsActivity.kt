package id.pasukanlangit.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.tourismapp.detail.DetailTourismActivity
import com.dicoding.tourismapp.di.MapsModuleDepedency
import com.google.gson.Gson
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import dagger.hilt.android.EntryPointAccessors
import id.pasukanlangit.core.data.Resource
import id.pasukanlangit.core.domain.model.Tourism
import id.pasukanlangit.maps.databinding.ActivityMapsBinding
import id.pasukanlangit.maps.di.DaggerMapsComponent
import id.pasukanlangit.maps.di.ViewModelFactory
import javax.inject.Inject


class MapsActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val binding: ActivityMapsBinding by viewBinding()
    private lateinit var mapboxmap: MapboxMap
    private val viewModel: MapsViewModel by viewModels {
        factory
    }

    private val REQUEST_CODE_ASK_PERMISSIONS = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_maps)

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

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            mapboxmap = mapboxMap
            getTourismData()
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val res = checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
//            if (res != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 123)
//            }else{
//                stepAfterSplash(savedInstanceState)
//            }
//        }
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            REQUEST_CODE_ASK_PERMISSIONS -> {
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(
//                        applicationContext,
//                        "READ_PHONE_STATE Denied",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                } else {
//                }
//                stepAfterSplash(null)
//            }
//            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//
//    }

//    private fun stepAfterSplash(savedInstanceState: Bundle?) {
//        binding.mapView.onCreate(savedInstanceState)
//        binding.mapView.getMapAsync { mapboxMap ->
//            mapboxmap = mapboxMap
//            getTourismData()
//        }
//    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun getTourismData() {
        viewModel.tourism.observe(this){ tourism ->
            if(tourism != null){
                binding.progressBar.isVisible = tourism is Resource.Loading
                when(tourism){
                    is Resource.Success -> {
                        binding.tvError.visibility = View.VISIBLE
                        showMarker(tourism.data)
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

    private fun showMarker(dataTourism: List<Tourism>?) {
        mapboxmap.setStyle(Style.MAPBOX_STREETS){ style ->
            style.addImage(ICON_ID, BitmapFactory.decodeResource(resources,R.drawable.mapbox_marker_icon_default))
            val latLngBoundsBuilder = LatLngBounds.Builder()

            val symbolManager = SymbolManager(binding.mapView, mapboxmap, style)
            symbolManager.iconAllowOverlap = true

            val options = ArrayList<SymbolOptions>()
            dataTourism?.forEach { data ->
                latLngBoundsBuilder.include(LatLng(data.latitude, data.longitude))
                options.add(
                    SymbolOptions()
                        .withLatLng(LatLng(data.latitude,data.longitude))
                        .withIconImage(ICON_ID)
                        .withData(Gson().toJsonTree(data))
                )
            }
            symbolManager.create(options)

            val latLngBounnds = latLngBoundsBuilder.build()
            mapboxmap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounnds,50), 5000)

            symbolManager.addClickListener { symbol ->
                val data = Gson().fromJson(symbol.data, Tourism::class.java)
                val intent = Intent(this, DetailTourismActivity::class.java)
                intent.putExtra(DetailTourismActivity.EXTRA_DATA, data)
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val ICON_ID = "ICON_ID"
    }
}