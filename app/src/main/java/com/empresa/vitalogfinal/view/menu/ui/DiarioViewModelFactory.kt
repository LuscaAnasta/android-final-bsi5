package com.empresa.vitalogfinal.view.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.empresa.vitalogfinal.repository.DiarioRepository

class DiarioViewModelFactory(
    private val repository: DiarioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiarioViewModel::class.java)) {
            return DiarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}