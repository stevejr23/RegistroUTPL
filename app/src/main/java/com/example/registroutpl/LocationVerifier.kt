package com.example.registroutpl

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*

class LocationVerifier : AppCompatActivity() {
    private lateinit var locationTextView: TextView // Ubicacion Actual
    private lateinit var locationStatusTextView: TextView // Estatus de la ubicación actual, si está dentro o fuera del rango
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private val desiredLatitude = -3.9870386238500495
    private val desiredLongitude = -79.19863502335933
    private val radius = 500.0 // Se especifica el radar de metros segun localizacion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationTextView = findViewById(R.id.location_text_view)
        locationStatusTextView = findViewById(R.id.location_status_text_view)
        getSpecificLocation()
    }

    private fun getSpecificLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    // Obtener Ubicación
                    locationTextView.text = "Latitude: $currentLatitude Longitude: $currentLongitude"
                    checkLocation(currentLatitude, currentLongitude)
                }
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun checkLocation(latitude: Double, longitude: Double) {
        val desiredLocation = Location("desired")
        desiredLocation.latitude = desiredLatitude
        desiredLocation.longitude = desiredLongitude

        val currentLocation = Location("current")
        currentLocation.latitude = latitude
        currentLocation.longitude = longitude

        val distance = desiredLocation.distanceTo(currentLocation)
        if (distance <= radius) {
            // Dentro del radio UTPL
            locationStatusTextView.text = "Se encuentra en la universidad"
            saveLocationToFirebase(latitude, longitude)
        } else {
            // Fuera de utpl
            locationStatusTextView.text = "Esta fuera de la universidad"
        }
    }

    // Guardar en firebase
    private fun saveLocationToFirebase(latitude: Double, longitude: Double) {
        val database = FirebaseDatabase.getInstance().reference
        val location = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )
        database.child("locations").push().setValue(location)
    }
}
