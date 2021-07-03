package com.dicoding.tourismapp.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.pasukanlangit.core.domain.usecase.TourismUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MapsModuleDepedency {
    fun tourismUseCase() : TourismUseCase
}