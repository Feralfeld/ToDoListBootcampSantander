package com.example.todolistbootcamp.models

data class ItemVO(
    var id: Int = -1,
    var idLista: Int = -1,
    var nome: String = "",
    var descricao: String = "",
    var isDated: Boolean = false,
    var valorData: String = "",
    var valorHora: String = "",
    var isAgenda: Boolean = false
)
