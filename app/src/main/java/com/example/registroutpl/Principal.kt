package com.example.registroutpl

import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import java.time.Instant
import java.time.LocalDateTime


class Principal : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        btn_salidaRegistro.isEnabled = false

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
        val formatter = SimpleDateFormat("E d MMM yyyy-HH:mm:ss" ,Locale("es", "ES"))
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
        val formatter = SimpleDateFormat("E d MMM yyyy-HH:mm:ss" ,Locale("es", "ES"))
        formatter.timeZone = TimeZone.getTimeZone("UTC-5")
        val current = formatter.format(Date())
        val separetedDate = current.split('-')
        val hora = separetedDate[1]
        val fecha = separetedDate[0]
        tv_horaRegistro.setText(hora)
        tv_fechaRegistro.setText(fecha)
    }

    fun setEntrada(email: String) {
        btn_entradaRegistro.setOnClickListener {
            val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                putExtra("email", email)
                putExtra("type", "entrada")
            }
            startActivity(saltar)
        }
    }

    fun setSalida(email: String) {
        btn_salidaRegistro.setOnClickListener {
            val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                putExtra("email", email)
                putExtra("type", "salida")
            }
            startActivity(saltar)
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
}