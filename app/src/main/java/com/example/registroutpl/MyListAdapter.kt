package com.example.registroutpl


import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyListAdapter(
    private val context: Activity,
    private val fecha: ArrayList<String>,
    private val entrada: ArrayList<String>,
    private val salida: ArrayList<String>


) : ArrayAdapter<String>(context, R.layout.activity_historial, fecha) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.activity_historial, null, true)

        val titleText = rowView.findViewById(R.id.tv_fecha) as TextView
        val entradaText = rowView.findViewById(R.id.tv_entrada) as TextView
        val salidaText = rowView.findViewById(R.id.tv_salida) as TextView

        titleText.text = fecha[position]
        entradaText.text = entrada[position]
        salidaText.text = salida[position]

        return rowView
    }
}