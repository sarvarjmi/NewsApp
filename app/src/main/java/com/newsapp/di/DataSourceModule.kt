package com.newsapp.di

import com.newsapp.data.local.datasource.LocalNewsDataSource
import com.newsapp.data.local.datasource.LocalNewsDataSourceImpl
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.datasource.RemoteNewsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteNewsDataSource(
        remoteNewsDataSourceImpl: RemoteNewsDataSourceImpl
    ): RemoteNewsDataSource

    @Binds
    @Singleton
    abstract fun bindLocalNewsDataSource(
        localNewsDataSourceImpl: LocalNewsDataSourceImpl
    ): LocalNewsDataSource
}
