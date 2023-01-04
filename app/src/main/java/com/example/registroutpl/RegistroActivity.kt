package com.example.registroutpl

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*

enum class ProviderType {
    BASIC,
}

class RegistroActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        setup()
        // Cargar una foto
        tvCargaFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        // Realizar una foto
        tvTomarFoto.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    private fun setup() {
        title = "Autentication"
        btnEnviar.setOnClickListener {
            if (etCorreo.text.isNotEmpty() && etContraseña.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        etCorreo.text.toString(),
                        etContraseña.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            register(it.result?.user?.email ?: "", ProviderType.BASIC.name)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de registro ")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showConfirmation(user: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage("Se ha registrado exitosamente el usuario $user ")
        builder.setPositiveButton(
            "Aceptar", DialogInterface.OnClickListener { dialog, id ->
                val saltar: Intent = Intent(this, LoginActivity::class.java)
                startActivity(saltar)
            })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun register(email: String, provider: String) {
        val usuario = etUsuario.text.toString();
        val phone = etPhone.text.toString();

        db.collection("users").document(email).set(
            hashMapOf(
                "provider" to provider,
                "usuario" to usuario,
                "phone" to phone,
            )
        )
        showConfirmation(usuario)
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

}

