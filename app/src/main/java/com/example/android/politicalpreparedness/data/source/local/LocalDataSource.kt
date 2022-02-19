package com.example.android.politicalpreparedness.data.source.local

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election

interface LocalDataSource {
    suspend fun getElections(): List<Election>

    suspend fun getElectionById(electionId: Int): Election

    suspend fun saveElection(election: Election)

    suspend fun deleteElection(electionId: Int)

    fun getAllSavedElections(): LiveData<List<SavedElectionInfo>>

    suspend fun saveFollowedElection(savedElectionInfo: SavedElectionInfo)

    suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo

    suspend fun deleteFollowedElection(savedElectionId: Int)

    suspend fun deleteAllElections()
}