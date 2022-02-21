package com.example.android.politicalpreparedness.data.source.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.model.SavedElectionInfo
import com.example.android.politicalpreparedness.data.source.remote.network.models.Election
import retrofit2.http.DELETE

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(vararg election: Election)


    @Query("SELECT * FROM election_table")
    suspend fun getElections(): List<Election>


    @Query("SELECT * FROM election_table WHERE id = :electionId")
    suspend fun getElectionById(electionId: Int): Election

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedElectionInfo(savedElectionInfo: SavedElectionInfo)

    @Query("SELECT * FROM saved_election_table")
    fun getAllSavedElections(): LiveData<List<SavedElectionInfo>>

    @Query("DELETE FROM saved_election_table WHERE id = :savedElectionId")
    suspend fun deleteSavedElection(savedElectionId: Int)

    @Query("SELECT * FROM saved_election_table WHERE id = :savedElectionId")
    suspend fun getSavedElectionById(savedElectionId: Int): SavedElectionInfo


    @Query("DELETE FROM election_table WHERE id = :electionId ")
    suspend fun deleteElection(electionId: Int)


    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()
}