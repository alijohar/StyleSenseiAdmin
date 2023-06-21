package com.example.stylesenseiadmin.model

data class ItemModel(
    val count: Int,
    val count_all: Int,
    val result: List<ItemResults>
)

data class ProductAttribute(
    val attr_name: String,
    val attr_value: String,
    val id: Int,
    val product_id: Int
)

data class ItemResults(
    val added_attr_count: Int,
    val corresponding_url: String,
    val created_at: String,
    val id: Int,
    val images: String?,
    val pictures:String,
    val modified_at: String,
    val name: String,
    val price: String,
    val product_attributes: List<ProductAttribute>,
    val resource: String?,
    val resource_id: Int,
    val type: Int
)