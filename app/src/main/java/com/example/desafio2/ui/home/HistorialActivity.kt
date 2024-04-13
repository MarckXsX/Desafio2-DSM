package com.example.desafio2.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.desafio2.R
import com.example.desafio2.ui.datos.Cita
import android.widget.Toast

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val listViewHistorial: ListView = findViewById(R.id.HistorialCita)

        // Recuperar los datos del historial de citas del Intent
        val historialCitas = intent.getSerializableExtra("historialCitas") as? ArrayList<Cita>

        if (historialCitas != null) {
            // Crear un adaptador personalizado para el historial de citas
            val adapter = HistorialCitasAdapter(this, historialCitas)
            listViewHistorial.adapter = adapter
        } else {
            // Manejar el caso en que no haya historial de citas disponible
            // Por ejemplo, mostrar un mensaje indicando que no hay citas registradas
            Toast.makeText(this, "No hay historial de citas disponible", Toast.LENGTH_SHORT).show()
        }
    }

    // Adaptador personalizado para el historial de citas
    class HistorialCitasAdapter(context: Context, private val historialCitas: List<Cita>) :
        ArrayAdapter<Cita>(context, R.layout.list_item_historial_citas, historialCitas) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listItemView = convertView
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_historial_citas, parent, false)
            }

            val currentCita = historialCitas[position]

            // Mostrar los datos de la cita en el ListView
            val textViewDescripcion: TextView = listItemView?.findViewById(R.id.textViewDescripcion) as TextView
            val textViewTotalPagar: TextView = listItemView.findViewById(R.id.textViewTotalPagar) as TextView

            textViewDescripcion.text = currentCita.descripcionTratamientos
            textViewTotalPagar.text = "Total: $${currentCita.totalPagar}"

            return listItemView
        }
    }
}