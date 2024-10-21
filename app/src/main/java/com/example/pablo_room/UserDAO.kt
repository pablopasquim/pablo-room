package com.example.pablo_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {

    @Insert
    suspend fun saveUser(user: User)

    @Query ("SELECT * FROM USER")
    suspend fun listUser() : List<User>

    @Update
    suspend fun attUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}