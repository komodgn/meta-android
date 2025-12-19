package com.example.metasearch.core.data.impl.util

internal object CypherQueryGenerator {

    /**
     * 특정 키워드(Entity)들을 모두 포함하는 사진을 찾는 쿼리 생성
     */
    fun generateQueryByKeywords(keywords: List<String>): String {
        if (keywords.isEmpty()) return ""

        val matchClauses = keywords.mapIndexed { i, keyword ->
            "(photo)-[]->(a$i {name: '$keyword'})"
        }.joinToString(", ")

        return "MATCH $matchClauses RETURN DISTINCT photo.name AS PhotoName"
    }

    /**
     * 키워드(Entity)와 관계(Relationship) 타입을 모두 지정하여 검색하는 쿼리 생성
     */
    fun generateQueryWithRelations(keywords: List<String>, relations: List<String>): String {
        val count = minOf(keywords.size, relations.size)
        if (count == 0) return ""

        val matchClauses = List(count) { i ->
            "(photo)-[:${relations[i]}]->(a$i:Entity {name: '${keywords[i]}'})"
        }.joinToString(", \n")

        return "MATCH $matchClauses\nRETURN photo.name AS PhotoName"
    }

    /**
     * 특정 관계(Relationship)만 포함하는 사진을 찾는 쿼리 생성
     */
    fun generateQueryByRelationTypes(relations: List<String>): String {
        if (relations.isEmpty()) return ""

        val matchClauses = relations.joinToString(", ") { rel ->
            "(photo)-[:$rel]->(entity)"
        }
        return "MATCH $matchClauses RETURN photo.name AS PhotoName"
    }
}
