package com.example.locationdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.locationdemo.databinding.ActivityMainBinding
import com.google.android.gms.common.api.*
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId: Int = 2

    private var mLocationManager: LocationManager? = null
    private val LOCATION_SETTING_REQUEST = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.visibility = View.GONE
        binding.btnNext.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TestDemo::class.java)
            startActivity(intent)
        })


        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingAlert()
            } else {
                getLocation()
            }
        }

        task.addOnFailureListener {
            Log.d("settingLocation", "FAILED")
        }

    }


    private val locationRequest = LocationRequest.Builder(100, 1000)
        .setIntervalMillis(1000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .build()
        .apply {
            val interval = 2000 // 2 seconds
            val fastestInterval = 1000 // 1 seconds
            val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private val locationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                // Handle location updates
                binding.tvLatLong.text =
                    "Lat Long: " + "\n" + locationResult.lastLocation?.latitude.toString() +
                            "\n" + locationResult.lastLocation?.longitude.toString()
                Log.d("Location Result", locationResult.lastLocation?.latitude.toString() + "Long:" + locationResult.lastLocation?.longitude.toString())
                Log.d("Location accuracy", locationResult.lastLocation?.accuracy.toString())
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                startLocationUpdates()

                /*mFusedLocationClient.lastLocation.addOnCompleteListener(this@MainActivity) { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastLocation = task.result
                        binding.tvLatLong.text = "Lat:" + (lastLocation)!!.latitude + "\nLong:" + (lastLocation)!!.longitude
                    } else {
                        Toast.makeText(this, "No Location detected", Toast.LENGTH_SHORT).show()
//                        getLocation()
                    }
                }*/
            } else {
                showSettingAlert()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingAlert()
        } else {
            getLocation()
        }

    }

    fun showSettingAlert() {
        /* val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
         alertDialog.setTitle("GPS is not Enabled!")
         alertDialog.setMessage("Do you want to turn on GPS?")
         alertDialog.setPositiveButton("Yes",
             DialogInterface.OnClickListener { dialog, which ->
                 val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                 this@MainActivity.startActivity(intent)

             })
         alertDialog.setNegativeButton("No",
             DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
         alertDialog.show()*/

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(applicationContext)
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                Toast.makeText(this@MainActivity, "GPS is On", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "checkSetting: GPS On")
            } catch (e: ApiException) {

                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                            this@MainActivity,
                            LOCATION_SETTING_REQUEST
                        )
                        Log.d(TAG, "checkSetting: RESOLUTION_REQUIRED")
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // USER DEVICE DOES NOT HAVE LOCATION OPTION
                    }
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTING_REQUEST -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startLocationUpdates()
                        Toast.makeText(this@MainActivity, "GPS is Turned on", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(
                            this@MainActivity,
                            "GPS is Required to use this app",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}



