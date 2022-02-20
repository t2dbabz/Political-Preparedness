package com.example.android.politicalpreparedness.ui.representative

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.ui.election.VoterInfoViewModel

class RepresentativeViewModelFactory(private val repository: Repository,
                                     owner: SavedStateRegistryOwner,
                                     defaultArgs: Bundle? = null):
    AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepresentativeViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}