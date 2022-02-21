package com.example.android.politicalpreparedness.ui.election

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.model.asElectionModel
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApiService
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch
import java.lang.Exception


class ElectionsViewModel(private val repository: Repository): ViewModel() {



    private val _elections = MutableLiveData<List<Election>?>()
    val election: LiveData<List<Election>?> = _elections

    private val _response = MutableLiveData<String>()
    val response : LiveData<String> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    val savedElections: LiveData<List<Election>> = Transformations.map(repository.getAllSavedElections()) {
        it.asElectionModel()
    }


    init {
        getElections()
    }


    fun getElections() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                when(val result = repository.getElections(false)) {

                    is Result.Success -> {
                        if (result.data != null && result.data.isNotEmpty()) {
                            val elections = result.data
                            _elections.value = elections
                            _isLoading.value = false
                        } else {
                            refreshElections()
                        }
                    }

                    is Result.Error ->{
                        _isLoading.value = false
                        _response.value = result.exception.toString()

                    }

                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            } catch (e: Exception){

            }

        }
    }

     fun refreshElections() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                when(val result = repository.getElections(true)) {

                    is Result.Success -> {
                        if (result.data != null) {
                            val elections = result.data
                            _elections.value = elections
                            _isLoading.value = false

                            repository.saveElection(elections)
                        } else {
                            _elections.value = null
                        }
                    }

                    is Result.Error ->{
                        _isLoading.value = false
                        _response.value = result.exception.toString()

                    }

                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            } catch (e: Exception){

            }

        }

    }


}