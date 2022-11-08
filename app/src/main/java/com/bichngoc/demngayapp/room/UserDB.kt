package com.bichngoc.demngayapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class UserDB(
    val nameMale: String,
    val nameFemale: String,
    val startDate: String,
    val dobMale: String,
    val dobFemale: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}
