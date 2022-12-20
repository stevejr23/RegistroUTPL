package com.example.registroutpl

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_crear_cuenta.*

enum class ProviderType {
    BASIC, // Registro Basico
    GOOGLE, // Registro con credenciales de google
    FACEBOOK  // Registro con credenciales de google

}

class CrearCuentaActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        val btnCrearCuenta = findViewById<Button>(R.id.btnEnviar)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")

        setup(email ?: "", provider ?: "")

        btnCrearCuenta.setOnClickListener {
            val saltar: Intent = Intent(this, LoginActivity::class.java)
            startActivity(saltar)
        }

        tvCargaFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        tvTomarFoto.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }


    // Cargar imagenes de la galeria
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                // Imagen seleccionada
                iVFoto.setImageURI(uri)
            } else {
                // No imagen
                Log.i("aris", "Imagen NO Seleccionada")
            }

        }

    //Evento que procesa el resultado de la cámara y envía la vista previa de la foto al ImageViewFoto
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val imageBitmap = intent?.extras?.get("data") as Bitmap
                val imageView = findViewById<ImageView>(R.id.iVFoto)
                imageView.setImageBitmap(imageBitmap)
            }
        }

    // Registro de usuarios
    private fun setup(email: String, provider: String) {
        title = "Inicio"
        tv_correo.text = email
        tv_provider.text = provider

        btn_salir.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        btnEnviar.setOnClickListener() {
            db.collection("users").document(email).set(
                hashMapOf(
                    "provider" to provider,
                    "usuario" to etUsuario.text.toString(),
                    "phone" to etPhone.text.toString(),
                )
            )
        }
    }
}



