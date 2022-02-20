package com.example.android.politicalpreparedness.ui.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: Repository) : ViewModel() {



    //TODO: Add live data to hold voter info

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo : LiveData<VoterInfoResponse> = _voterInfo

    private val _electionInfoUrl = MutableLiveData<String?>()
    val electionInfoUrl: LiveData<String?> = _electionInfoUrl

    private val _votingLocationFinderUrl = MutableLiveData<String?>()
    val votingLocationFinderUrl: LiveData<String?> = _votingLocationFinderUrl

    private val _ballotInfoUrl = MutableLiveData<String?>()
    val ballotInfoUrl : LiveData<String?> = _ballotInfoUrl


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean> = _isElectionFollowed

    private val _electionId = MutableLiveData<Int>()




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
                        if (result.data != null) {
                        _voterInfo.value = result?.data!!
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

    fun getElectionInformationUrl() {
        _electionInfoUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
    }

    fun getVotingLocationsUrl() {
        _votingLocationFinderUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }
    fun getBallotInformationUrl() {
        _ballotInfoUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }


    fun urlInformationNavigated() {
        _electionInfoUrl.value = null
        _votingLocationFinderUrl.value = null
        _ballotInfoUrl.value = null
    }

    fun checkIfElectionFollowed(electionId: Int) {
        viewModelScope.launch {
            _electionId.value = electionId
            val result = repository.getSavedElectionById(electionId)
            _isElectionFollowed.value = result != null
        }
    }


     fun followElection(){
        viewModelScope.launch {
            if (_isElectionFollowed.value == true) {
                _electionId.value?.let { repository.deleteFollowedElection(it) }
                _voterInfo.value?.election?.let { checkIfElectionFollowed(it.id) }
            } else {
                _voterInfo.value?.let { voterInfo ->
                    val savedElectionInfo = SavedElectionInfo(
                        id = voterInfo.election.id,
                        name = voterInfo.election.name,
                        electionDay = voterInfo.election.electionDay,
                        division = voterInfo.election.division,
                    )
                    repository.saveFollowedElection(savedElectionInfo)
                }
                _voterInfo.value?.election?.let { checkIfElectionFollowed(it.id) }
            }

        }
    }











}