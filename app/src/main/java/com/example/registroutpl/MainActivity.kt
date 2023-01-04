package com.example.registroutpl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIniciar.setOnClickListener {
            val saltar: Intent = Intent(this, LoginActivity::class.java)
            startActivity(saltar)
        }

        btnRegistro.setOnClickListener {
            val saltar: Intent = Intent(this, RegistroActivity::class.java)
            startActivity(saltar)
        }
    }
}