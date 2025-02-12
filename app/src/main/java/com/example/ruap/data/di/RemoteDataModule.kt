package com.example.ruap.data.di

import com.example.ruap.data.repository.ResultRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteDataModule = module {

    single {
        HttpClient(
            OkHttp
        ) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

        }
    }

    single {
        ResultRepository(get())
    }
}