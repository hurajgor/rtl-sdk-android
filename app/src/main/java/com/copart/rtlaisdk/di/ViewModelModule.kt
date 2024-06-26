package com.copart.rtlaisdk.di

import com.copart.rtlaisdk.ui.rtlList.RTLListViewModel
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        RTLListViewModel(rtlRepository = get())
    }
    viewModel {
        VehicleDetailsViewModel(rtlRepository = get())
    }
}
