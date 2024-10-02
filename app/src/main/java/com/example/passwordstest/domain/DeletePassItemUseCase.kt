package com.example.passwordstest.domain

class DeletePassItemUseCase(private val passListRepository: PassListRepository) {
    fun deletePassItem(passItem: PassItem){
        passListRepository.deletePassItem(passItem)
    }
}