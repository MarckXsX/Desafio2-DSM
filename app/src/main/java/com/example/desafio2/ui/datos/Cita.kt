package com.example.desafio2.ui.datos

import java.io.Serializable

class Cita : Serializable {
    var descripcionTratamientos: String? = null
    var totalPagar: Double? = null



    constructor()

    constructor(descripcionTratamientos: String?, totalPagar: Double?) {
        this.descripcionTratamientos = descripcionTratamientos
        this.totalPagar = totalPagar
    }
}
