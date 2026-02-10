package com.example.domain.di

import com.example.domain.service.GuessService
import com.example.domain.service.ScoreService
import com.example.domain.service.TimerService
import com.example.domain.service.WordService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    @ViewModelScoped
    fun provideTimerService(
        @com.example.common.di.ViewModelCoroutineScope
        coroutineScope: CoroutineScope
    ): TimerService {
        return TimerService(coroutineScope)
    }

    @Provides
    @ViewModelScoped
    fun provideWordService(): WordService {
        return WordService()
    }

    @Provides
    @ViewModelScoped
    fun provideScoreService(): ScoreService {
        return ScoreService()
    }

    @Provides
    @ViewModelScoped
    fun provideGuessService(): GuessService {
        return GuessService()
    }
}