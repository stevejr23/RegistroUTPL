package com.example.registroutpl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_principal.*

class Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        btn_entradaRegis.setOnClickListener {
            val saltar: Intent = Intent(this, ProcesoRegistro::class.java)
            startActivity(saltar)
        }
    }
}