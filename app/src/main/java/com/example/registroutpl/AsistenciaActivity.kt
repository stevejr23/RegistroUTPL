package com.example.registroutpl


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_asistencia.*

enum class ProviderType {
    BASIC,
}

class AsistenciaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")

        setup(email ?: "", provider ?: "")
    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        tv_correo.text = email
        tv_provider.text = provider

        btn_salir.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}