package com.metasearch.android.core.room.api.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "input_name")
    val inputName: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = "",

    @ColumnInfo(name = "is_home_display")
    val isHomeDisplay: Boolean = false,

    @ColumnInfo(name = "representative_face_id")
    val representativeFaceId: Long? = null,
)
