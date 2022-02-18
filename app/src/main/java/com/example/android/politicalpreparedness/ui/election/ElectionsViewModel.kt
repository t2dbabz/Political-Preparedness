package com.example.android.politicalpreparedness.ui.election

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApiService
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch
import java.lang.Exception

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(): ViewModel() {

    val repository = RemoteDataSourceImpl(CivicsApi.retrofitService)

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    private val _elections = MutableLiveData<List<Election>?>()
    val election: LiveData<List<Election>?> = _elections

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getElections()
//        getElect()
    }


    fun getElections() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                when(val result = repository.getElections()) {

                    is Result.Success -> {
                        if (result.data != null) {
                            Log.d("ElectionViewModel", result.data.toString())
                            val elections = result.data
                            _elections.value = elections
                            _isLoading.value = false
                        }
                    }

                    is Result.Error ->{
                        _isLoading.value = false
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