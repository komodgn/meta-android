package com.example.metasearch.core.room.api.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.metasearch.core.room.api.entity.FaceEntity
import com.example.metasearch.core.room.api.entity.PersonEntity

data class PersonWithFaces(
    @Embedded
    val person: PersonEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "person_id",
    )
    val faces: List<FaceEntity>,
)
