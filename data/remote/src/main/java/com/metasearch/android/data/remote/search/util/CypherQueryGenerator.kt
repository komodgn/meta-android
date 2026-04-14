package com.metasearch.android.data.remote.search.util

object CypherQueryGenerator {

    fun generateQueryByKeywords(keywords: List<String>): String {
        if (keywords.isEmpty()) return ""

        val matchClauses = keywords.mapIndexed { i, keyword ->
            "(photo)-[]->(a$i {name: '$keyword'})"
        }.joinToString(", ")

        return "MATCH $matchClauses RETURN DISTINCT photo.name AS PhotoName"
    }

    fun generateQueryWithRelations(keywords: List<String>, relations: List<String>): String {
        val count = minOf(keywords.size, relations.size)
        if (count == 0) return ""

        val matchClauses = List(count) { i ->
            "(photo)-[:${relations[i]}]->(a$i:Entity {name: '${keywords[i]}'})"
        }.joinToString(", \n")

        return "MATCH $matchClauses\nRETURN photo.name AS PhotoName"
    }

    fun generateQueryByRelationTypes(relations: List<String>): String {
        if (relations.isEmpty()) return ""

        val matchClauses = relations.joinToString(", ") { rel ->
            "(photo)-[:$rel]->(entity)"
        }
        return "MATCH $matchClauses RETURN photo.name AS PhotoName"
    }
}
