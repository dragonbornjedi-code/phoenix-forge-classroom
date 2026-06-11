package com.phoenixforge.student.di

import android.content.Context
import androidx.room.Room
import com.phoenixforge.student.data.local.StudentDatabase
import com.phoenixforge.student.data.local.dao.StudentDao
import com.phoenixforge.student.data.repository.StudentRepositoryImpl
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.session.StudentSessionStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudentDatabaseModule {

    private const val PREFS_STUDENT_SESSION = "student_session"

    @Provides
    @Singleton
    fun provideStudentSessionStore(@ApplicationContext context: Context): StudentSessionStore {
        val prefs = context.getSharedPreferences(PREFS_STUDENT_SESSION, Context.MODE_PRIVATE)
        return StudentSessionStore(prefs)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StudentDatabase =
        Room.databaseBuilder(context, StudentDatabase::class.java, "student_house.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(database: StudentDatabase): StudentDao = database.studentDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: StudentRepositoryImpl): StudentRepository
}
