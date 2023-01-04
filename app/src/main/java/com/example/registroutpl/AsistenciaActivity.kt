package com.example.registroutpl


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_asistencia.*
import kotlinx.android.synthetic.main.activity_asistencia.tv_correo
import kotlinx.android.synthetic.main.activity_asistencia.tv_provider
import kotlinx.android.synthetic.main.activity_crear_cuenta.*

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
        btnCrearCuenta.setOnClickListener() {
            if (etCorreo.text.isNotEmpty() &&
                etContraseña.text.isNotEmpty()
            ) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        etUsuario.text.toString(),
                        etCorreo.text.toString(),
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                            //println(it.result)
                        }
                    }
            }
        }
    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticando al usuario ")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent: Intent = Intent(this, AsistenciaActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}