package com.example.passwordsapp.feature_pass.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}