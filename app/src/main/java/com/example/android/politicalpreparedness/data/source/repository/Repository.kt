package com.example.android.politicalpreparedness.data.source.repository

import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result

interface Repository {
    suspend fun getElections() : Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Long) : Result<VoterInfoResponse>
}