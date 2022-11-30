package com.example.mypet.googlemaps.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import com.example.mypet.googlemaps.*
import com.example.mypet.googlemaps.model.MapClusterItem
import com.example.mypet.googlemaps.model.PlaceDetailsItem
import com.example.mypet.googlemaps.model.UserLocation
import com.example.mypet.googlemaps.util.getStatusBarHeight
import com.example.mypet.googlemaps.util.safeNavigate
import com.example.mypet.googlemaps.util.screenWidth

@AndroidEntryPoint
class MapSearcherFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    override fun getViewBinding(): FragmentMapBinding = FragmentMapBinding.inflate(layoutInflater)

    private val activityViewModel: AppViewModel by activityViewModels()

    private val viewModel: MapSearcherViewModel by viewModels()

    override fun getStatusBarType(): StatusBarType = StatusBarType.LIGHT

    private var googleMap: GoogleMap? = null

    private var clusterManager: ClusterManager<MapClusterItem>? = null

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
            showGooglePlacesUI.observe(viewLifecycleOwner) {
                showItemsOnMap(it)
            }
            showUserLocationOnMapUI.observe(viewLifecycleOwner, Observer(::showUserLocationOnMapUI))
            initUserLocationUI.observe(viewLifecycleOwner) {
                initUserLocationUI()
            }
            showGooglePlaceDetailsUI.observe(viewLifecycleOwner){
                showClusterItemDetails(it)
            }
        }
    }

    override fun setupViews() {}

    override fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.isMyLocationButtonEnabled = false
        }

        setupClusterManager()

        (activity as? AppActivity)?.checkForLocationServices()
    }


    private fun setupClusterManager() {
        clusterManager = ClusterManager<MapClusterItem>(requireContext(), googleMap).apply {
            setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener { item ->
                getClusterItemDetails(item)
                return@OnClusterItemClickListener true
            })
            renderer = MapSearcherClusterRenderer(
                context = requireContext(),
                clusterItemWidth = (resources.screenWidth * CLUSTER_ITEM_WIDTH_PERCENT).toInt(),
                map = googleMap,
                clusterManager = this
            )
            googleMap?.setOnCameraIdleListener(this)
            googleMap?.setOnMarkerClickListener(this)
        }
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

    private fun getClusterItemDetails(mapClusterItem: MapClusterItem) {
        with(binding) {
            viewModel.googlePlaceDetails(mapClusterItem.id)
        }
    }

    private fun showClusterItemDetails(placeDetailsItem: PlaceDetailsItem) {
        with(binding) {
            excavationSiteDetailsCard.visibility = View.VISIBLE
            storesAddressTxt.text = placeDetailsItem.formattedΑddress
            storesNameLabelTxt.text = placeDetailsItem.name
            storesScheduleTxt.text = placeDetailsItem.phone
        }
    }

    private fun showItemsOnMap(items: List<MapClusterItem>) {
        clusterManager?.apply {
            clearItems()
            addItems(items)
            cluster()
        }
    }

    /*private fun showCurrentLocation(locationPair: Pair<MapPinCoordinates?, Float?>) {
        val cameraBuild = CameraPosition.builder().apply {
            target(locationPair.first?.let { LatLng(it.latitude, it.longitude) } ?: INITIAL_LOCATION)
            zoom(locationPair.second ?: INITIAL_ZOOM)
        }.build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraBuild))
    }*/

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