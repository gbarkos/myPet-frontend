package com.example.mypet.googlemaps.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.example.mypet.googlemaps.model.MapClusterItem
import com.example.mypet.R

class MapSearcherClusterRenderer(
    val context: Context,
    private val clusterItemWidth: Int,
    map: GoogleMap?,
    clusterManager: ClusterManager<MapClusterItem>?
) : DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MapClusterItem?, markerOptions: MarkerOptions?) {
        item?.let {
            bitmapDescriptorFromVector(context, R.drawable.ic_cluster_item_pin)?.let { bitmap ->
                markerOptions?.icon(bitmap)
            }
        }
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {
        ContextCompat.getDrawable(context, vectorDrawableResourceId)?.let {
            it.setBounds(0, 0, clusterItemWidth, clusterItemWidth)
            val bitmap: Bitmap = Bitmap.createBitmap(clusterItemWidth, clusterItemWidth, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            it.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        return null
    }

    override fun getColor(clusterSize: Int): Int {
        return ContextCompat.getColor(context, R.color.purple_500)
    }
}