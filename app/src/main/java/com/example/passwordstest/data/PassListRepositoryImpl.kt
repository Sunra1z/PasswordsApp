package com.example.passwordstest.data

import com.example.passwordstest.domain.PassItem
import com.example.passwordstest.domain.PassListRepository

object PassListRepositoryImpl: PassListRepository {

    private val passList = mutableListOf<PassItem>()

    private var autoIncrementId = 0

    override fun addPassItem(passItem: PassItem) {
        if (passItem.id == PassItem.UNDEFINED_ID) {
            passItem.id = autoIncrementId++
        }
        passList.add(passItem)
    }

    override fun deletePassItem(passItem: PassItem) {
        passList.remove(passItem)
    }

    override fun editPassItem(passItem: PassItem) {
        val oldElement = getPassById(passItem.id)
        passList.remove(oldElement)
        addPassItem(passItem)
    }

    override fun getPassById(passId: Int): PassItem {
        // change NullException later
        return passList.find { it.id == passId } ?: throw RuntimeException ("Element with id $passId not found")
    }

    override fun getPassList(): List<PassItem> {
        return passList.toList()
    }
}