package com.example.passwordstest.domain

data class PassItem(
    var id: Int = UNDEFINED_ID, // change when switching to DB
    val name: String,
    val pass: String
) {

    companion object {

        const val UNDEFINED_ID = -1
    }
}
