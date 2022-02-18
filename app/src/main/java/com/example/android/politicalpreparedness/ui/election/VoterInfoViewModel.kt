package com.example.android.politicalpreparedness.ui.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.source.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel() : ViewModel() {

    val repository = RemoteDataSourceImpl(CivicsApi.retrofitService)

    //TODO: Add live data to hold voter info

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo : LiveData<VoterInfoResponse> = _voterInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    fun getVoterInfo(address: String, electionId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                when (val result = repository.getVoterInfo(address, electionId)) {
                    is Result.Success -> {
                        if (result != null) {
                        _voterInfo.value = result.data!!
                        _isLoading.value  = false
                        Log.d("VoterInfoViewModel", result.data.state.toString())

                        }

                    }

                    is Result.Error -> {
                        _isLoading.value = false
                        Log.d("VoterInfoViewModel", "${result.exception}")
                    }

                    is  Result.Loading -> {
                        _isLoading.value = true
                        Log.d("VoterInfoViewModel", "Loading")
                    }
                }
            } catch (e: Exception) {

            }
        }
    }





}