package com.example.android.politicalpreparedness.data.source.repository

import androidx.lifecycle.LiveData
import androidx.room.Update
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result

interface Repository {
    suspend fun getElections(refresh: Boolean) : Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Long) : Result<VoterInfoResponse>
    suspend fun getRepresentatives(address: String) : Result<RepresentativeResponse>
    suspend fun saveElection(elections: List<Election>)
    fun getAllSavedElections(): LiveData<List<SavedElectionInfo>>
    suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo
    suspend fun saveFollowedElection(savedElectionInfo: SavedElectionInfo)
    suspend fun deleteFollowedElection(savedElectionId: Int)
}