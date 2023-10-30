package com.nagaja.app.android

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.nagaja.app.android.Utils.NagajaLog


class GpsTracker(context: Context) : Service(), LocationListener {

    private lateinit var mContext: Context
    private var mLocation: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()

    private var locationManager: LocationManager? = null

    init {
        this.mContext = context
        getLocation()
    }

    @JvmName("getLocation1")
    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                return null
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                } else {
                    return null
                }

                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)

                    if (locationManager != null) {
                        mLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                        latitude = mLocation!!.latitude
                        longitude = mLocation!!.longitude
                    }
                }
                if (isGPSEnabled) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    if (locationManager != null) {
//                        location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                        val location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                    }
                }
            }
        } catch (e: Exception) {
            NagajaLog().d("wooks, Get Location Error = ${e.toString()}")
        }

        return mLocation
    }

    fun getLatitude(): Double? {
        if (null == mLocation) {
            return null
        }

        latitude = mLocation!!.latitude
        return latitude
    }

    fun getLongitude(): Double? {
        if (null == mLocation) {
            return null
        }

        longitude = mLocation!!.longitude
        return longitude
    }

    override fun onLocationChanged(location: Location) {
//        TODO("Not yet implemented")
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }
}