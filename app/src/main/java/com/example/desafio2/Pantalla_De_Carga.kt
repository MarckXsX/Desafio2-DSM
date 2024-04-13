package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Pantalla_De_Carga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_de_carga)

        supportActionBar?.hide()
        val Tiempo = 4000

        Handler().postDelayed({
            startActivity(Intent(this@Pantalla_De_Carga, MainActivity::class.java))
            finish()
        }, Tiempo.toLong())
    }
}