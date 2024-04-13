package com.example.desafio2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.desafio2.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import android.widget.ArrayAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.desafio2.ui.datos.Tratamiento
import com.example.desafio2.ui.datos.Cita
import com.example.desafio2.R


import android.content.Context
import android.content.Intent
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var tratamientoList: MutableList<Tratamiento>
    private lateinit var adapter: ArrayAdapter<Tratamiento>

    private val tratamientosSeleccionados: MutableList<Tratamiento> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Colocar codigo aqui
        val fab_cita : FloatingActionButton = root.findViewById(R.id.fab_cita)
        val fab_historial : FloatingActionButton = root.findViewById(R.id.fab_historial)

        auth = FirebaseAuth.getInstance()
        val ListaTratamiento : ListView = root.findViewById(R.id.ListaTratamiento)
        tratamientoList = mutableListOf()

        val adapter = TratamientoAdapter(requireContext(), tratamientoList)
        ListaTratamiento.adapter = adapter

        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refTratamientos: DatabaseReference = database.getReference("Tratamiento")

        refTratamientos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tratamientoList.clear()
                for (tratamientoSnapshot in dataSnapshot.children) {
                    val tratamiento = tratamientoSnapshot.getValue(Tratamiento::class.java)
                    tratamiento?.let {
                        it.key = tratamientoSnapshot.key // Asignar la clave del tratamiento

                        tratamientoList.add(it)
                    }
                }
                adapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de lectura de la base de datos aquí
            }
        })

        ListaTratamiento.setOnItemClickListener { parent, view, position, id ->
            // Obtener el tratamiento seleccionado
            val tratamientoSeleccionado = tratamientoList[position]

            // Agregar el tratamiento seleccionado a la lista de tratamientos seleccionados
            tratamientosSeleccionados.add(tratamientoSeleccionado)

            Toast.makeText(requireContext(), "Tratamiento seleccionado: ${tratamientoSeleccionado.key}", Toast.LENGTH_SHORT).show()
        }

        fab_cita.setOnClickListener{
            val intent = Intent(requireContext(), CitaActivity::class.java)
            intent.putExtra("tratamientosSeleccionados", ArrayList(tratamientosSeleccionados))
            startActivity(intent)
            tratamientosSeleccionados.clear()
        }

        fab_historial.setOnClickListener{
            // Obtener datos del historial de citas desde Firebase
            val refHistorialCitas = FirebaseDatabase.getInstance().getReference("Citas")
            refHistorialCitas.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val historialCitas: MutableList<Cita> = mutableListOf()
                    for (citaSnapshot in dataSnapshot.children) {
                        val cita = citaSnapshot.getValue(Cita::class.java)
                        cita?.let {
                            historialCitas.add(it)
                        }
                    }

                    // Pasar datos al activity HistorialActivity
                    val intent = Intent(requireContext(), HistorialActivity::class.java)
                    intent.putExtra("historialCitas", ArrayList(historialCitas))
                    startActivity(intent)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores de lectura de la base de datos aquí
                }
            })
        }






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TratamientoAdapter(context: Context, private val tratamientos: List<Tratamiento>) :
        ArrayAdapter<Tratamiento>(context, android.R.layout.simple_list_item_1, tratamientos) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listItemView = convertView
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            }

            val currentTratamiento = tratamientos[position]

            // Construir la cadena para mostrar la descripción y el precio
            val descripcionPrecio = "${currentTratamiento.descripcion} \nPrecio: $${currentTratamiento.precio}"

            val textView = listItemView?.findViewById<TextView>(android.R.id.text1)
            textView?.text = descripcionPrecio

            return listItemView!!
        }
    }
}