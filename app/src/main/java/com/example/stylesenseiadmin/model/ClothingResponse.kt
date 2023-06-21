package com.example.stylesenseiadmin.model

data class ClothingResponse(
    val clothes: List<Clothing>
)

data class Clothing(
    val type: String,
    val attrs: List<Map<String, List<String>>>
)
