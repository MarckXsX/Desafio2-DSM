package com.example.desafio2.ui.datos
import java.io.Serializable

class Tratamiento : Serializable  {
    var descripcion: String? = null
    var precio: Double? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(descripcion: String?, precio: Double?, key: String?, per: MutableMap<String, Boolean>) {
        this.descripcion = descripcion
        this.precio = precio
        this.key = key
        this.per = per
    }
}