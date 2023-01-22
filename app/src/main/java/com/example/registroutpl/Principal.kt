package com.example.registroutpl

import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*

class Principal : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")

        getUserName(email ?: "")
        getHour()
        setEntrada(email?:"", provider?:"")
        setSalida(email?:"", provider?:"")
    }


    fun getUserName(email: String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            tv_nombreRegistro.setText(it.get("usuario") as String?)
        }
    }

    fun getHour() {
        val dateFormat = SimpleDateFormat("E d MMM yyyy-HH:mm:ss", Locale("es"))
        val completeDate = dateFormat.format(Date())
        val separetedDate = completeDate.split('-')
        val hour = separetedDate[1]
        val date = separetedDate[0]
        tv_horaRegistro.setText(hour)
        tv_fechaRegistro.setText(date)
    }

    fun setEntrada(email:String, provider:String) {
        btn_entradaRegistro.setOnClickListener {
            val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
                putExtra("type", "entrada")
            }
            startActivity(saltar)
        }
    }

    fun setSalida(email: String, provider: String) {
        btn_entradaRegistro.setOnClickListener {
            val saltar: Intent = Intent(this, ProcesoRegistro::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
                putExtra("type", "salida")
            }
            startActivity(saltar)
        }
    }

}