package com.example.android.politicalpreparedness.data.source.repository

import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSource
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Repository {
    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher)  {

        when(val response = remoteDataSource.getElections()) {

            is Result.Success -> {
                if (response.data != null) {
                    Result.Success(response.data)
                } else {
                    Result.Success(null)
                }

            }

            is Result.Error -> {
                Result.Error(response.exception)
            }

            else -> {
                Result.Loading
            }

        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Long): Result<VoterInfoResponse> = withContext(ioDispatcher) {

        when(val response = remoteDataSource.getVoterInfo(address, electionId)) {
            is Result.Success -> {
                if (response.data != null) {
                    Result.Success(response.data)
                } else {
                    Result.Success(null)
                }
            }

            is Result.Error -> {
                Result.Error(response.exception)
            }

            is Result.Loading -> {
                Result.Loading
            }
        }

    }
}