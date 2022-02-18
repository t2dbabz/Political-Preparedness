package com.example.android.politicalpreparedness

import android.content.Context
import com.example.android.politicalpreparedness.data.source.local.LocalDataSourceImpl
import com.example.android.politicalpreparedness.data.source.local.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSource
import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
//import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApiService
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.data.source.repository.RepositoryImpl

object ServiceLocator {

    @Volatile
    var repository: Repository? = null


    fun provideRepository(context: Context): Repository {
        synchronized(this){
            return repository ?: createRepository(context)
        }
    }


     private fun createRepository(context: Context): Repository {
         val database : ElectionDatabase by lazy {
             ElectionDatabase.getInstance(context)
         }
        val newRepo = RepositoryImpl(RemoteDataSourceImpl(CivicsApi.retrofitService), LocalDataSourceImpl(database.electionDao))
        repository = newRepo
        return newRepo
    }

}