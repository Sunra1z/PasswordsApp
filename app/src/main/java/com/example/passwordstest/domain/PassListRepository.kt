package com.example.passwordstest.domain

interface PassListRepository {
    fun addPassItem(passItem: PassItem)

    fun deletePassItem(passItem: PassItem)

    fun editPassItem()

    fun getPassById(passId: Int): PassItem

    fun getPassList(): List<PassItem>

}
