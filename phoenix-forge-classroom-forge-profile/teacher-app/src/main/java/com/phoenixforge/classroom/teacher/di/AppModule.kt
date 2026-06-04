package com.phoenixforge.classroom.teacher.di

import android.content.Context
import androidx.room.Room
import com.phoenixforge.classroom.teacher.data.local.IntentTileDao
import com.phoenixforge.classroom.teacher.data.local.TeacherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): TeacherDatabase =
        Room.databaseBuilder(ctx, TeacherDatabase::class.java, "teacher_edition.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideIntentTileDao(db: TeacherDatabase): IntentTileDao = db.intentTileDao()
}
