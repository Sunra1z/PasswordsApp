package com.example.passwordstest.domain

class AddPassItemUseCase(private val passListRepository: PassListRepository) {
    fun addPassItem(passItem: PassItem){
        passListRepository.addPassItem(passItem)
    }
}