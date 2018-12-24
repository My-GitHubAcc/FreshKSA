package com.nibrasco.freshksa.Model

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.support.v4.app.ActivityCompat

class GPSTracker(private val context: Context) : Service(), LocationListener {
    internal var isGPSEnabled = false
    internal var isNetworkEnabled = false
    internal var canGetLocation = false

    internal var location: Location? = null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()

    internal var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    private fun getLocation(): Location? {
        // TODO Auto-generated method stub
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null
            }
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true

                if (isNetworkEnabled) {

                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 3f, this)

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 3f, this)
                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this@GPSTracker)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            dialog.cancel()
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }

        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }

        return longitude
    }

    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    override fun onLocationChanged(location: Location?) {
        // TODO Auto-generated method stub
        if (location != null) {
            this.location = location
        }
    }

    override fun onProviderDisabled(provider: String) {
        // TODO Auto-generated method stub

    }

    override fun onProviderEnabled(provider: String) {
        // TODO Auto-generated method stub

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // TODO Auto-generated method stub

    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

}
