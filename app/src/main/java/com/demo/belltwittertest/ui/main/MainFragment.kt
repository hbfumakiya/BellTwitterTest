package com.demo.belltwittertest.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.demo.belltwittertest.R
import com.demo.belltwittertest.utils.Const.DEFAULT_RADIUS
import com.demo.belltwittertest.utils.PermissionValidator
import com.demo.belltwittertest.utils.PermissionValidator.hasLocationAccess
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


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
    private var zoomScale=10F
    private lateinit var circle: Circle


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        val view=inflater.inflate(R.layout.main_fragment, container, false)

        val mapFragment =childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        radiusView=view.findViewById(R.id.radiusView)
        radiusView.text="${radius/1000} KM"


        seekBar=view.findViewById(R.id.seekBar)

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                radius=progress*1000.0
                radiusView.text="${radius/1000} KM"
                circle.remove()
                addMapCircle(mLocation)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }

        if(hasLocationAccess(requireContext())){
            mLocation=requestLocation()

            mLocation.let {
                mMap.apply {
                    addMarker(MarkerOptions().position(mLocation).title("You are here")).isVisible=true
                    moveCamera(CameraUpdateFactory.newLatLng(mLocation))
                    animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation,zoomScale)))
                    uiSettings.isMyLocationButtonEnabled=false
                }
                addMapCircle(mLocation)
            }
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
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation(): LatLng {
        val locationManager=activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        return LatLng(location.latitude,location.longitude)
     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
