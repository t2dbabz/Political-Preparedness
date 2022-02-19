package com.example.android.politicalpreparedness.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.local.LocalDataSource
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSource
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
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

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> = withContext(ioDispatcher){
        when(val response = remoteDataSource.getRepresentatives(address)) {
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


    override fun getAllSavedElections(): LiveData<List<SavedElectionInfo>> {
        return  localDataSource.getAllSavedElections()
    }

    override suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo = withContext(ioDispatcher) {
        localDataSource.getSavedElectionById(savedElectionId)
    }

    override suspend fun saveFollowedElection(savedElectionInfo: SavedElectionInfo) = withContext(ioDispatcher) {
        localDataSource.saveFollowedElection(savedElectionInfo)
    }

    override suspend fun deleteFollowedElection(savedElectionId: Int) = withContext(ioDispatcher){
        localDataSource.deleteFollowedElection(savedElectionId)
    }


}