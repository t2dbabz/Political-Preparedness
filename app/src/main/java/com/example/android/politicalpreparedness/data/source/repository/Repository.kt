package com.example.android.politicalpreparedness.data.source.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result

interface Repository {
    suspend fun getElections() : Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Long) : Result<VoterInfoResponse>
    fun getAllSavedElections(): LiveData<List<SavedElectionInfo>>
    suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo
    suspend fun saveFollowedElection(savedElectionInfo: SavedElectionInfo)
    suspend fun deleteFollowedElection(savedElectionId: Int)
}