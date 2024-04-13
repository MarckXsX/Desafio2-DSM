package com.example.desafio2.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.desafio2.R
import com.example.desafio2.ui.datos.Tratamiento
import com.example.desafio2.ui.datos.Cita
import com.google.firebase.database.FirebaseDatabase

class CitaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita)

        val listViewTratamientos: ListView = findViewById(R.id.ListaCita)
        val textViewTotal : TextView = findViewById(R.id.txtTotal)
        val btnLimpiarCita: Button = findViewById(R.id.btnLimpiarCita)
        val btnAgendarCita : Button = findViewById(R.id.btnAgendarCita)

        val tratamientosSeleccionados = intent.getSerializableExtra("tratamientosSeleccionados") as? ArrayList<Tratamiento>
        if (tratamientosSeleccionados != null) {
            val adapter = TratamientoAdapter(this, tratamientosSeleccionados)
            listViewTratamientos.adapter = adapter

            // Calcular el total
            var totalPagar = 0.0
            for (tratamiento in tratamientosSeleccionados) {
                totalPagar += tratamiento.precio ?: 0.0
            }
            textViewTotal.text = "${String.format("%.2f", totalPagar)}"

            btnLimpiarCita.setOnClickListener {
                if (tratamientosSeleccionados.isEmpty()) {
                    Toast.makeText(this, "No hay tratamientos seleccionados para limpiar", Toast.LENGTH_SHORT).show()
                } else {
                    tratamientosSeleccionados.clear()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Cita Eliminada", Toast.LENGTH_SHORT).show()
                    textViewTotal.text = "0.0"
                    finish()
                }
            }

            btnAgendarCita.setOnClickListener {
                if (tratamientosSeleccionados.isEmpty()) {
                    Toast.makeText(this, "No hay tratamientos seleccionados para agendar cita", Toast.LENGTH_SHORT).show()
                } else {
                    val descripcionTratamientos = tratamientosSeleccionados.joinToString(separator = ", ") { it.descripcion ?: "" }
                    val totalPagarString = textViewTotal.text.toString()
                    val totalPagar = totalPagarString.toDouble()
                    println(totalPagar)

                    // Crear objeto de cita
                    val cita = Cita(descripcionTratamientos, totalPagar)

                    // Obtener referencia a la base de datos de Firebase
                    val database = FirebaseDatabase.getInstance()
                    val refCitas = database.getReference("Citas")

                    // Generar una nueva clave única para la cita
                    val nuevaCitaKey = refCitas.push().key ?: ""

                    // Almacenar la cita en la base de datos
                    refCitas.child(nuevaCitaKey).setValue(cita)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Cita agendada correctamente", Toast.LENGTH_SHORT).show()
                            // Limpiar tratamientos seleccionados después de agendar la cita
                            tratamientosSeleccionados.clear()
                            adapter.notifyDataSetChanged()
                            textViewTotal.text = "$0.0"
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al agendar la cita", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        } else {
            // Manejar el caso en que la lista sea nula o esté vacía
        }
    }

    class TratamientoAdapter(context: Context, private val tratamientos: List<Tratamiento>) :
        ArrayAdapter<Tratamiento>(context, R.layout.list_item_tratamiento, tratamientos) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listItemView = convertView
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_tratamiento, parent, false)
            }



            val currentTratamiento = tratamientos[position]

            val textViewDescripcion: TextView = listItemView?.findViewById(R.id.textViewDescripcion) as TextView
            val textViewPrecio: TextView = listItemView.findViewById(R.id.textViewPrecio) as TextView

            textViewDescripcion.text = currentTratamiento.descripcion
            textViewPrecio.text = "Precio: $${currentTratamiento.precio}"

            /*val descripcionPrecio = "${currentTratamiento.descripcion} \nPrecio: $${currentTratamiento.precio}"

            val textView = listItemView?.findViewById<TextView>(android.R.id.text1)
            textView?.text = descripcionPrecio*/

            return listItemView!!
        }
    }
}