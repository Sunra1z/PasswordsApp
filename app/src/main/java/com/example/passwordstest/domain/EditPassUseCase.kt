package com.example.passwordstest.domain

class EditPassUseCase(private val passListRepository: PassListRepository) {
    fun editPassItem(passItem: PassItem){
        passListRepository.editPassItem()
    }
}