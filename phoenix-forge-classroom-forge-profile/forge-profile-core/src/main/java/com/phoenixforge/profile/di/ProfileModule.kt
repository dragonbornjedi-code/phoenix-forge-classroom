package com.phoenixforge.profile.di

import android.content.Context
import androidx.room.Room
import com.phoenixforge.profile.data.local.ProfileDatabase
import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.data.repository.ProfileRepositoryImpl
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.domain.auth.AuthProvider
import com.phoenixforge.profile.domain.auth.LocalSecureAuthProvider
import com.phoenixforge.profile.domain.repository.ProfileRepository
import com.phoenixforge.profile.domain.session.ProfileSessionStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    private const val PREFS_STEWARD_SECURE = "steward_secure"
    private const val PREFS_PROFILE_SESSION = "profile_session"

    @Provides
    @Singleton
    fun provideForgeProfileJson(): ForgeProfileJson = ForgeProfileJson()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProfileDatabase {
        return Room.databaseBuilder(
            context,
            ProfileDatabase::class.java,
            "forge_profile.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: ProfileDatabase): ProfileDao = database.dao

    @Provides
    @Singleton
    fun provideRepository(
        dao: ProfileDao,
        forgeProfileJson: ForgeProfileJson
    ): ProfileRepository = ProfileRepositoryImpl(dao, forgeProfileJson)

    @Provides
    @Singleton
    fun provideAuthProvider(
        @ApplicationContext context: Context
    ): AuthProvider {
        val prefs = context.getSharedPreferences(PREFS_STEWARD_SECURE, Context.MODE_PRIVATE)
        return LocalSecureAuthProvider(prefs)
    }

    @Provides
    @Singleton
    fun provideProfileSessionStore(
        @ApplicationContext context: Context
    ): ProfileSessionStore {
        val prefs = context.getSharedPreferences(PREFS_PROFILE_SESSION, Context.MODE_PRIVATE)
        return ProfileSessionStore(prefs)
    }
}
