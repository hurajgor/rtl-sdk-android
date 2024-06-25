package com.copart.rtlaisdk.di

import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.data.RTLRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    factory<RTLRepository> {
        RTLRepositoryImpl(
            rtlApi = get()
        )
    }

}