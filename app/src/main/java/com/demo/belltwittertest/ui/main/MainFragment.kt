package com.demo.belltwittertest.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.demo.belltwittertest.R
import com.demo.belltwittertest.utils.*
import com.demo.belltwittertest.utils.Const.DEFAULT_RADIUS
import com.demo.belltwittertest.utils.Const.DEFAULT_ZOOM_SCALE
import com.demo.belltwittertest.utils.PermissionValidator.PERMISSION_REQUEST
import com.demo.belltwittertest.utils.PermissionValidator.hasLocationAccess
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.runBlocking


class MainFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var seekBar: SeekBar
    private lateinit var radiusView:TextView
    private var radius= DEFAULT_RADIUS
    private lateinit var mLocation:LatLng
    private lateinit var circle: Circle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        val view=inflater.inflate(R.layout.main_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        radiusView=view.findViewById(R.id.radiusView)
        seekBar=view.findViewById(R.id.seekBar)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }

        if(hasLocationAccess(requireContext())){
            requestLocation()
        }else {
            PermissionValidator.requestPermission(activity as Activity)
        }

    }

    private fun addMapCircle(mLocation: LatLng) {
        circle=mMap.addCircle(CircleOptions()
            .center(mLocation)
            .radius(radius)
            .strokeWidth(2f)
            .fillColor(0x550000FF))

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation,
            getZoomLevel(circle))))
    }


    private fun requestLocation() {
        val locationManager=activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location:Location?=null

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
         location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if(location!=null){
            runBlocking {
                CacheLoader.setLocation(location)
            }

            mLocation= LatLng(location.latitude,location.longitude)
            mLocation.let {
                mMap.apply {
                    addMarker(MarkerOptions().position(mLocation).title("You are here")).isVisible=true
                    moveCamera(CameraUpdateFactory.newLatLng(mLocation))
                    animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation,DEFAULT_ZOOM_SCALE)))
                    uiSettings.isMyLocationButtonEnabled=false
                }
                addMapCircle(mLocation)
            }
        }

     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        radiusView.text="${radius/1000} KM"

        CacheLoader.setRadius(radius)

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                radius=progress*1000.0
                CacheLoader.setRadius(radius)
                radiusView.text="${radius/1000} KM"
                circle.remove()
                addMapCircle(mLocation)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val mapFragment =childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        if(isNetworkAvailable(requireContext())){
            mapFragment.getMapAsync(this)
        }
        else{
            mapFragment.getMapAsync(this)
            Toast.makeText(requireContext(),"Please check network Connection",Toast.LENGTH_SHORT).show()
        }
    }

}
