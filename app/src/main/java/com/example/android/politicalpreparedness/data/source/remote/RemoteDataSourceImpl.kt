package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApiService
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import com.example.android.politicalpreparedness.data.source.remote.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Result
import com.example.android.politicalpreparedness.utils.Result.Success
import com.example.android.politicalpreparedness.utils.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RemoteDataSourceImpl(
    private val apiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RemoteDataSource {
    override suspend fun getElections(): Result<List<Election>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getElections()
                if (result.elections.isNotEmpty()) {
                    val elections = result.elections
                    Success(elections)
                } else {
                    Success(null)
                }
            } catch (e: Exception) {
                Error(e)
            }
    }

    override suspend fun getVoterInfo(address: String, electionId: Long): Result<VoterInfoResponse> =
        withContext(ioDispatcher)  {
            return@withContext try {
                val result = apiService.getVoterInfo(address, electionId)
                run {
                    Success(result)
                }
            } catch (e: Exception) {
                Error(e)
            }

    }
}