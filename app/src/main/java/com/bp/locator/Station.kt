package com.bp.locator

data class Station(
    val name: String,
    val address: String,
    val distance: Double, // in miles
    val open24Hours: Boolean,
    val hasConvenienceStore: Boolean,
    val servesHotFood: Boolean,
    val acceptsBpFuelCards: Boolean
) 