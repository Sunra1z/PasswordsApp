package com.example.passwordstest.domain

class GetPassListUserCase(private val passListRepository: PassListRepository) {
    fun getPassList(): List<PassItem>{
        return passListRepository.getPassList()
    }

}