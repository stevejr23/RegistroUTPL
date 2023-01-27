package com.example.registroutpl

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.activity_proceso_registro.*
import kotlinx.android.synthetic.main.activity_registro.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ProcesoRegistro : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val type: String? = bundle?.getString("type")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proceso_registro)

        btn_tomarfotoRegis.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            subirData(email ?: "", type ?: "")
        }

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val imageBitmap = intent?.extras?.get("data") as Bitmap
                iv_foto_registro2.setImageBitmap(imageBitmap)
                btn_tomarfotoRegis.setText("Repetir Foto")
                btn_subir_data.visibility = View.VISIBLE
            }
        }


    private fun subirData(email: String, registrationType: String) {
        val date = Date()
        val formatter = SimpleDateFormat("E d MMM yyyy-HH:mm:ss",Locale("es", "ES"))
        val current = formatter.format(date)
        val separetedDate = current.split('-')
        val hora = separetedDate[1]
        val fecha = separetedDate[0]

        val col = db.collection(email).document(fecha)

        if (registrationType == "entrada") {
            col.set(
                hashMapOf(
                    "entrada" to hora,
                    "salida" to null
                )

            )
        } else {
            col.update(
                "salida", hora
            )
        }

        btn_subir_data.setOnClickListener {
            showConfirmation(registrationType, email)
        }
    }

    // Alerta de que se creo correctamente
    private fun showConfirmation(resgistrationType: String, email: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro Exitoso")
        builder.setMessage("Se ha registrado exitosamente la ${resgistrationType} ")
        builder.setPositiveButton(
            "Aceptar", DialogInterface.OnClickListener { dialog, id ->
                val saltar: Intent = Intent(this, Principal::class.java).apply {
                    putExtra("email", email)
                }
                startActivity(saltar)
            })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

