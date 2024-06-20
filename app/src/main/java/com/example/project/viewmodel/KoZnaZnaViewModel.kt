package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.project.model.dao.KoZnaDao
import kotlinx.coroutines.Dispatchers

class KoZnaZnaViewModel(private val koZnaDao: KoZnaDao) : ViewModel() {

    val questions = liveData(Dispatchers.IO) {
        val data = koZnaDao.getRandomQuestions()
        emit(data)
    }
}

class KoZnaZnaViewModelFactory(private val koZnaDao: KoZnaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KoZnaZnaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KoZnaZnaViewModel(koZnaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
