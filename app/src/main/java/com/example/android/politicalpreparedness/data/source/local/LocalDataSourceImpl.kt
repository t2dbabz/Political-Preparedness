package com.example.android.politicalpreparedness.data.source.local

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalDataSource {
    override suspend fun getElections(): List<Election> = withContext(ioDispatcher) {
        return@withContext electionDao.getElections()
    }

    override suspend fun getElectionById(electionId: Int): Election = withContext(ioDispatcher){
        return@withContext electionDao.getElectionById(electionId)
    }

    override suspend fun saveElection(elections: List<Election>) = withContext(ioDispatcher) {
        electionDao.insertElection(*elections.toTypedArray())

    }

    override suspend fun deleteElection(electionId: Int) = withContext(ioDispatcher) {
       electionDao.deleteElection(electionId)
    }

    override suspend fun deleteAllElections() = withContext(ioDispatcher) {
        electionDao.deleteAllElections()
    }

    override fun getAllSavedElections(): LiveData<List<SavedElectionInfo>> {
        return  electionDao.getAllSavedElections()
    }

    override suspend fun saveFollowedElection(savedElectionInfo: SavedElectionInfo) = withContext(ioDispatcher) {
        electionDao.insertSavedElectionInfo(savedElectionInfo)
    }

    override suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo  = withContext(ioDispatcher){
        return@withContext electionDao.getSavedElectionById(savedElectionId)
    }

    override suspend fun deleteFollowedElection(savedElectionId: Int) = withContext(ioDispatcher) {
        electionDao.deleteSavedElection(savedElectionId)
    }


}