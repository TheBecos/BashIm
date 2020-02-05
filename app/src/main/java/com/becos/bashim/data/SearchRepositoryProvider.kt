package com.becos.bashim.data

object SearchRepositoryProvider {

    fun provideSearchRepository():SearchRepository{
        return SearchRepository(BashImApiService.create())
    }
}