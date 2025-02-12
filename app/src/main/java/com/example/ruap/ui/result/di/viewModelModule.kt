package com.example.ruap.ui.result.di

import com.example.ruap.ui.result.ResultViewModel
import com.example.ruap.ui.result.model.ResultScreenData
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        (resultScreenData: ResultScreenData) ->
        ResultViewModel(resultScreenData, get(),get())
    }
}