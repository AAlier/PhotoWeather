package com.photoweather.common.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import timber.log.Timber
import java.io.IOException
import java.util.Locale

class LocationManager constructor(
    private val context: Context,
) {

    companion object {
        private val REQUEST_CHECK_SETTINGS = 101

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10_000

        /**
         * The fastest rate for active location updates. Exact.
         * Updates will never be more frequent than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private val mSettingsClient: SettingsClient by lazy {
        LocationServices.getSettingsClient(context)
    }
    var onLocationChanged: (location: Location?, address: String) -> Unit = { _, _ -> }

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private val mLocationRequest: LocationRequest by lazy {
        LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is
            // inexact. You may not receive updates at all if no location sources are available, or
            // you may receive them slower than requested. You may also receive updates faster than
            // requested if other applications are requesting location at a faster interval.
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates faster than this value.
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private val mLocationSettingsRequest: LocationSettingsRequest by lazy {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        builder.addLocationRequest(mLocationRequest)
        builder.build()
    }

    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            onLocationChanged(location, getAddress(location!!))
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    fun startLocationUpdates(activity: Activity) {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(activity) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Check permission but I already check permission before use this class
                    return@addOnSuccessListener
                }
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback!!, Looper.myLooper()
                )
            }
            .addOnFailureListener(activity) { e ->
                val statusCode: Int = (e as ApiException).statusCode
                handleStartLocationFailureCases(activity, e as ResolvableApiException, statusCode)
            }
    }

    private fun handleStartLocationFailureCases(activity: Activity, e: ResolvableApiException, statusCode: Int) {
        when (statusCode) {
            RESOLUTION_REQUIRED -> {
                Timber.d("Location settings are not satisfied. Attempting to upgrade location settings")
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the
                    // result in onActivityResult().
                    e.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                } catch (sie: IntentSender.SendIntentException) {
                    Timber.d("PendingIntent unable to execute request.")
                }
            }

            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                Timber.e("Location settings are inadequate, and cannot be fixed here. Fix in Settings.")
            }

            else -> {
            }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    fun stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mLocationCallback?.let { mFusedLocationClient.removeLocationUpdates(it) }
        mLocationCallback = null
    }

    fun getAddress(location: Location): String {
        var address = ""
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)!!
            if (addresses.isNotEmpty()) {
                address = addresses[0].locality
            }
        } catch (e: IOException) {
            Timber.e(e, "Error retrieving location")
            e.printStackTrace()
        }
        return address
    }
}