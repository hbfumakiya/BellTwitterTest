package com.demo.belltwittertest.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.demo.belltwittertest.R
import com.demo.belltwittertest.adapter.MyInfoViewAdapter
import com.demo.belltwittertest.utils.*
import com.demo.belltwittertest.utils.Const.DEFAULT_RADIUS
import com.demo.belltwittertest.utils.Const.DEFAULT_ZOOM_SCALE
import com.demo.belltwittertest.utils.PermissionValidator.hasLocationAccess
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.runBlocking


class MainFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var mLocation:LatLng
    private  var location:Location?=null
    private lateinit var circle: Circle

    private var circleRadius= DEFAULT_RADIUS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.tweetList.observe(this, Observer {
            loadTweetsOnMap(it)
        })



    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }

        activity?.let {
            if(hasLocationAccess(it)){
                requestLocation()
            }else {
                PermissionValidator.requestPermission(it)
            }
        }

    }

    private fun addMapCircle(mLocation: LatLng) {
        circle=mMap.addCircle(CircleOptions()
            .center(mLocation)
            .radius(circleRadius)
            .strokeWidth(2f)
            .fillColor(0x220000FF))

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation,getZoomLevel(circle))))
    }


    private fun requestLocation() {

        location=getLastKnownLocation()
        location?.let {
            CacheLoader.setLocation(it)

            mLocation= LatLng(it.latitude, it.longitude)
            mLocation.let {
                mMap.apply {
                    moveCamera(CameraUpdateFactory.newLatLng(mLocation))
                    animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation,DEFAULT_ZOOM_SCALE)))
                    setInfoWindowAdapter(activity?.let { it1 -> MyInfoViewAdapter(it1) })
                    uiSettings.isMyLocationButtonEnabled=false
                }

            }
            viewModel.getNearbyTweets(it,circleRadius.toFloat())

        }

     }


    private fun getLastKnownLocation(): Location? {
        activity?.let {
            val locationManager=it.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            return if(ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            else
                null
        }
        return null

    }

    private fun loadTweetsOnMap(nearbyTweets: MutableList<Tweet>?) {
        nearbyTweets?.map {
            Pair(it,it.getLatLng())
        }?.forEach {
            addMarker(it)
        }
        addMapCircle(mLocation)

    }

    private fun addMarker(tweet: Pair<Tweet, LatLng?>) {
        mMap.apply {
            val json=tweet.first.toJSON()
            tweet.second?.let {
                addMarker( MarkerOptions().position(it)
                    .title(tweet.first.user.name)
                    .snippet(json.toString()))
            }
        }

    }

    private fun initUI() {

        setRadius(DEFAULT_RADIUS)

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setRadius(progress*1000.0)
                mMap.clear()
                addMapCircle(mLocation)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                location?.let { viewModel.getNearbyTweets(it,circleRadius.toFloat()) }
            }
        })

        val mapFragment =childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        activity?.let {
            if(!isNetworkAvailable(it))
                Toast.makeText(it,R.string.check_internet,Toast.LENGTH_SHORT).show()
        }

    }
    private fun setRadius(radius:Double){
        circleRadius=radius
        radiusView.text="${circleRadius/1000} KM"
        CacheLoader.setRadius(circleRadius)
    }

}

