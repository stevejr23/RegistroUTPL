package com.example.registroutpl

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.lista.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

class Historial : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    var fecha = arrayListOf<String>()
    val entrada = arrayListOf<String>()
    val salida = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")

        getHistorial(email ?: "")

        btn_back.setOnClickListener {
            val saltar: Intent = Intent(this, Principal::class.java).apply {
                putExtra("email", email)
            }
            startActivity(saltar)
        }

    }

    fun getHistorial(email: String) {
        db.collection(email)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    fecha.add(document.id)
                    entrada.add(document.data["entrada"] as String)
                    if (document.data["salida"] != null){
                        salida.add(document.data["salida"] as String)
                    }else{
                        salida.add("--:--:--")
                    }
                }
                showHistory()
            }
    }

    fun showHistory() {
        val myListAdapter = MyListAdapter(this, fecha, entrada, salida)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(
                this,
                "Click on item at $itemAtPos its item id $itemIdAtPos",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
