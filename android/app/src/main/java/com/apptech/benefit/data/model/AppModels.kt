package com.apptech.benefit.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSummary(
    val id: String,
    val name: String,
    val link: String,
    val iconUrl: String? = null,
    val category: String,
    val benefitCount: Int,
)

@Serializable
data class Benefit(
    val title: String,
    val description: String,
    val howTo: List<String>,
)

@Serializable
data class AppDetail(
    val id: String,
    val name: String,
    val link: String,
    val iconUrl: String? = null,
    val category: String,
    val benefits: List<Benefit>,
)
