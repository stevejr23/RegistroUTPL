package com.example.registroutpl

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import java.time.Instant
import java.time.LocalDateTime


class Principal : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val desiredLatitude = -3.9870386238500495
    private val desiredLongitude = -79.19863502335933
//    private val desiredLatitude =  37.4220936
//    private val desiredLongitude = -122.083922
    private val radius = 500.0 // Se especifica el radar de metros segun localizacion
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        btn_salidaRegistro.isEnabled = false

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getUserName(email ?: "")
        getHour()
        getRegistration(email ?: "")
        setEntrada(email ?: "")
        setSalida(email ?: "")
        historial(email ?: "")
    }


    fun getUserName(email: String) {

        db.collection("users").document(email).get().addOnSuccessListener {
            tv_nombreRegistro.setText(it.get("usuario") as String?)
        }

    }


    fun getRegistration(email: String) {
        val date = Date()
        val formatter = SimpleDateFormat("E d MMM yyyy-HH:mm:ss", Locale("es", "ES"))
        val current = formatter.format(date)
        val separetedDate = current.split('-')
        val fecha = separetedDate[0]

        db.collection(email).document(fecha).get().addOnSuccessListener {
            val entrada = it.get("entrada")
            val salida = it.get("salida")
            if (entrada != null) {
                tv_horaentradaRegistro.setText(it.get("entrada") as String)
                btn_salidaRegistro.isEnabled = true
                btn_entradaRegistro.isEnabled = false
            }
            if (salida != null) {
                tv_horaSalidaRegistro.setText(it.get("salida") as String)
                btn_salidaRegistro.isEnabled = false
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHour() {
        // zona horaria
        val date = Date()
        val formatter = SimpleDateFormat("E d MMM yyyy-HH:mm:ss", Locale("es", "ES"))
        val current = formatter.format(date)
        val separetedDate = current.split('-')
        val hora = separetedDate[1]
        val fecha = separetedDate[0]
        tv_horaRegistro.setText(hora)
        tv_fechaRegistro.setText(fecha)
    }

    fun setEntrada(email: String) {
        btn_entradaRegistro.setOnClickListener {
            findLocation(email,"entrada")
        }
    }

    fun setSalida(email: String) {
        btn_salidaRegistro.setOnClickListener {
            findLocation(email,"salida")
        }
    }

    fun historial(email: String) {
        btn_historial.setOnClickListener {
            val saltar: Intent = Intent(this, Historial::class.java).apply {
                putExtra("email", email)
            }
            startActivity(saltar)
        }
    }

    private fun checkLocation(latitude: Double, longitude: Double):Boolean{
        val desiredLocation = Location("desired")
        desiredLocation.latitude = desiredLatitude
        desiredLocation.longitude = desiredLongitude

        val currentLocation = Location("current")
        currentLocation.latitude = latitude
        currentLocation.longitude = longitude

        val distance = desiredLocation.distanceTo(currentLocation)
        return distance <= radius
    }

    private fun showErrorMessage(email: String, type: String, latitude: String, longitude: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta de Ubicacion")
        builder.setMessage("Usted no se encuentra dentro de la universidad \n¿Desea continuar? ")
        builder.setPositiveButton(
            "Continuar", DialogInterface.OnClickListener { dialog, id ->
                val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                    putExtra("email", email)
                    putExtra("type", type)
                    putExtra("longitud", longitude)
                    putExtra("latitud", latitude)
                }
                startActivity(saltar)
            })
        builder.setNegativeButton("Cancelar") { dialog, id ->
            val saltar: Intent = Intent(this, Principal::class.java).apply {
                putExtra("email", email)
            }
            startActivity(saltar)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun findLocation(email:String, type: String){
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        task.addOnSuccessListener {
            val latitude = it.latitude.toString()
            val longitude = it.longitude.toString()
            if (it != null) {
                if (checkLocation(it.latitude, it.longitude)) {
                    val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                        putExtra("email", email)
                        putExtra("type", type)
                        putExtra("latitud", latitude)
                        putExtra("longitud", longitude)
                    }
                    startActivity(saltar)
                } else {
                    showErrorMessage(email, type, latitude, longitude)
                }
            }
        }
    }
}