package com.example.registroutpl

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.*
import kotlinx.android.synthetic.main.activity_registro.*
import java.io.ByteArrayOutputStream

enum class ProviderType {
    BASIC,
}

class RegistroActivity : AppCompatActivity() {

    lateinit var storage: FirebaseFirestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Ingreso
        setup()

        // Cargar una foto
        tvCargaFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Realizar una foto
        tvTomarFoto.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        // Subir imagen
        //storage = Firebase.storage
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

    // Alerta de que no se registro correctamente
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de registro ")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Alerta de que se creo correctamente
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

    // Registro del usuario con correo,phone y usuario
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

    // Evento que procesa el resultado de la cámara y envía la vista previa de la foto al ImageViewFoto
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val imageBitmap = intent?.extras?.get("data") as Bitmap
                val imageView = findViewById<ImageView>(R.id.iVFoto)
                imageView.setImageBitmap(imageBitmap)
            }
        }

    // subir imagen al firebase storage
    private fun subirImagen() {
        val storage = Firebase.storage
        var storageRef = storage.reference
        val rutaImagen = storageRef.child("registroAvatar/avatar.jpg")

        // Get the data from an ImageView as bytes
        tvCargaFoto.isDrawingCacheEnabled = true
        tvCargaFoto.buildDrawingCache()
        val bitmap = (tvCargaFoto.drawableState as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()

        var uploadTask = rutaImagen.putBytes(data)
        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->

        }

    }
}

