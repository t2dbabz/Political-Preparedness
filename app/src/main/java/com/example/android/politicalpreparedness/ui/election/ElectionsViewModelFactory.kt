package com.example.android.politicalpreparedness.ui.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.source.repository.Repository
import java.lang.IllegalArgumentException

class ElectionsViewModelFactory(private val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        @Suppress("UNCHECKED_CAST")
        (ElectionsViewModel(repository) as T)

}