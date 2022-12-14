package com.example.mypet.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPointBinding
import com.example.mypet.googlemaps.model.UserLocation
import com.example.mypet.googlemaps.ui.*
import com.example.mypet.googlemaps.util.getStatusBarHeight
import com.example.mypet.googlemaps.util.safeNavigate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PointFinderFragment : BaseFragment<FragmentPointBinding>(), OnMapReadyCallback {

    override fun getViewBinding(): FragmentPointBinding = FragmentPointBinding.inflate(layoutInflater)

    private val activityViewModel: AppViewModel by activityViewModels()

    private val viewModel: MapSearcherViewModel by viewModels()

    override fun getStatusBarType(): StatusBarType = StatusBarType.LIGHT

    private var googleMap: GoogleMap? = null

    private var lat = 0.0
    private var long = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding.outdoorMapView.onCreate(savedInstanceState)
            binding.outdoorMapView.getMapAsync(this)
        } catch (e: Exception) {
            activity?.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as? AppActivity)?.handleLocationPermissions()
        binding.outdoorMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.outdoorMapView.onResume()
    }

    override fun onPause() {
        binding.outdoorMapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.outdoorMapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.outdoorMapView.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.outdoorMapView.onLowMemory()
    }

    override fun setupObservers() {
        with(activityViewModel) {
            locationServicesUI.observe(viewLifecycleOwner) {
                viewModel.initUserLocation()
            }
        }
        with(viewModel) {
            showUserLocationOnMapUI.observe(viewLifecycleOwner, Observer(::showUserLocationOnMapUI))
            initUserLocationUI.observe(viewLifecycleOwner) {
                initUserLocationUI()
            }
        }
    }

    override fun setupViews() {}

    override fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.isMyLocationButtonEnabled = false
        }

        googleMap?.setOnMapClickListener { point ->
            googleMap?.clear()
            googleMap?.addMarker(MarkerOptions().position(point))
            lat = point.latitude
            long = point.longitude
            binding.joinNowBtn.visibility = View.VISIBLE
        }
        binding.joinNowBtn.setOnClickListener {
            onClick()
        }
        (activity as? AppActivity)?.checkForLocationServices()
    }

    @SuppressLint("MissingPermission")
    private fun initUserLocationUI() {
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isCompassEnabled = false
        viewModel.centerOnUserLocation()
        binding.myLocationBtn.apply {
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = resources.getStatusBarHeight() + resources.getDimensionPixelSize(R.dimen.margin_half_8dp)
            }
            setOnClickListener {
                viewModel.centerOnUserLocation()
            }
        }
    }

    private fun onClick(){
        val intent = Intent()
        intent.putExtra("Lat", lat)
        intent.putExtra("Long", long)

        (this.requireActivity() as AppActivity).setResult(10, intent) //The data you want to send back

        (this.requireActivity() as AppActivity).finish()
    }

    private fun showUserLocationOnMapUI(location: UserLocation) {
        val cameraBuild = CameraPosition.builder().apply {
            target(LatLng(location.latitude, location.longitude))
            zoom(USER_LOCATION_ZOOM)
        }.build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraBuild))
    }

    /*
    * Navigation
    */
    private fun navigateUI(directions: NavDirections) {
        findNavController().safeNavigate(directions, R.id.mapSearcherFragment)
    }


    companion object {
        private val INITIAL_LOCATION = LatLng(40.63713547036048, 22.946366871423827)
        private const val INITIAL_ZOOM = 11.5f
        private const val USER_LOCATION_ZOOM = 13f
        private const val CLUSTER_ITEM_WIDTH_PERCENT = 0.1
    }
}