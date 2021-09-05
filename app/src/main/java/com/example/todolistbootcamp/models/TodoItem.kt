package com.example.todolistbootcamp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoItem (
    var nome: String,
    var descricao: String,
    var id: Int,
    var isDated: Boolean,
    var valorData: String ,
    var valorHora: String ,
    var isAgenda: Boolean
) : Parcelable