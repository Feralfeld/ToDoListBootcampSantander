package com.example.todolistbootcamp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyList (
    var nome: String,
    var descricao: String,
    var id: Int
) : Parcelable