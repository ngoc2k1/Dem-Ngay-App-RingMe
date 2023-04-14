package com.bichngoc.demngayapp.room

import android.content.Context
import androidx.room.*

@Database(entities = [UserDB::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "users_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
