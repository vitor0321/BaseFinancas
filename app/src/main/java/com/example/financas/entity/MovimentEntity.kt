package com.example.financas.entity

import com.example.financas.enums.MovimentType
import com.example.financas.extensions.getFormattedCurrency

class MovimentEntity(
    val name: String,
    val description: String?,
    val type: MovimentType,
    val value: Double
) {
    override fun toString(): String {
        return "Nome $name\n" +
                "Descrição  $description\n" +
                "Tipo ${type.value}\n" +
                "Valor  ${value.getFormattedCurrency()}"
    }
}