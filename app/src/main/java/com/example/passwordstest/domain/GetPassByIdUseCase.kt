package com.example.passwordstest.domain

class GetPassByIdUseCase(private val passListRepository: PassListRepository) {
    fun getPassById(passId: Int): PassItem{
        return passListRepository.getPassById(passId)
    }
}