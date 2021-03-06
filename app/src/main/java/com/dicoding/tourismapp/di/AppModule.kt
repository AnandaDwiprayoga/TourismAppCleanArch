package com.dicoding.tourismapp.di

import id.pasukanlangit.core.domain.usecase.TourismInteractor
import id.pasukanlangit.core.domain.usecase.TourismUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideTourismUseCase(tourismInteractor: TourismInteractor): TourismUseCase
}