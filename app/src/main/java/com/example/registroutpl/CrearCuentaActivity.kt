package com.example.registroutpl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_crear_cuenta.*

class CrearCuentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val saltar: Intent = Intent(this, LoginActivity::class.java)
            startActivity(saltar)
        }
        tvCargaFoto.setOnClickListener {
            val saltar: Intent = Intent(this, LoginActivity::class.java)
            startActivity(saltar)
        }
        tvTomarFoto.setOnClickListener {
            val saltar: Intent = Intent(this, LoginActivity::class.java)
            startActivity(saltar)
        }
    }
}