package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result

interface RemoteDataSource {
    suspend fun getElections(): Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Long): Result<VoterInfoResponse>
    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>
}