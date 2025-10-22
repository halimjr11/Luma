package com.halimjr11.luma.view.feature.maps

import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.ActivityMapsBinding
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.ui.helper.launchAndCollect
import com.halimjr11.luma.utils.UiState
import com.halimjr11.luma.view.feature.maps.di.loadMapModule
import com.halimjr11.luma.view.feature.maps.viewmodel.MapViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MapsActivity : AppCompatActivity(), AndroidScopeComponent, OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapViewModel by viewModel()
    private var googleMap: GoogleMap? = null

    override val scope: Scope by activityScope()

    init {
        loadMapModule()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        observeData()
    }

    private fun setupUI() = with(binding) {
        val mapFragment = supportFragmentManager
            .findFragmentById(map.id) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)
    }

    private fun observeData() = with(viewModel) {
        launchAndCollect(mapStory) { state ->
            when (state) {
                is UiState.Success -> {
                    println("Jalanan ==> ${state.data}")
                    setupMap(state.data)
                }

                else -> Unit
            }
        }
    }

    private fun setupMap(data: List<StoryDomain>) {
        val dataMap = data.map {
            val latLng = LatLng(it.lat, it.lon)
            val owner = it.name
            val desc = it.description
            Triple(latLng, owner, desc)
        }

        dataMap.forEach { (latLng, owner, desc) ->
            googleMap?.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(owner)
                    .snippet(desc)
                    .icon(bitmapDescriptorFromVector(R.drawable.ic_pin))
            )
        }

        val bounds = LatLngBounds.Builder().apply {
            dataMap.forEach { include(it.first) }
        }.build()

        val padding = 120
        googleMap?.setOnMapLoadedCallback {
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    padding
                )
            )
        }
    }

    private fun bitmapDescriptorFromVector(@DrawableRes vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)!!
        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        )
        setupMap(viewModel.list)
    }
}