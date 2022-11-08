package com.bichngoc.demngayapp.room

import androidx.room.*

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserDB>

    @Insert
    suspend fun insertUser(user: UserDB)//1 record

    @Update
    suspend fun updateUser(user: UserDB)//1 record

//    @Delete
//    suspend fun deleteUser(user: UserDB)//1 record

    @Query("DELETE FROM users")
    suspend fun deleteUser()//1 table

    @Query("SELECT * FROM users WHERE _id = :id")
    suspend fun getUserById(id: Int): UserDB
}