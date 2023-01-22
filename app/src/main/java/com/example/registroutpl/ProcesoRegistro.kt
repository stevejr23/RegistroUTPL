package com.example.registroutpl

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.activity_proceso_registro.*

class ProcesoRegistro : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        val type: String? = bundle?.getString("type")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proceso_registro)

        btn_tomarfotoRegis.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
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
                subirData()
            }
        }


    fun subirData() {
        btn_subir_data.setOnClickListener {
            val saltar: Intent = Intent(this, Principal::class.java)
            startActivity(saltar)
        }
    }
}