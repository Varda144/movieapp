package com.movieapp

import android.app.Application
import com.movieapp.data.local.AppDatabase
import com.movieapp.data.repo.MovieRepository

class MovieApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { MovieRepository(database.favoriteDao()) }
}
