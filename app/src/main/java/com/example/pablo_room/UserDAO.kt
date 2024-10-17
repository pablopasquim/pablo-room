package com.example.pablo_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert
    suspend fun saveUser(user: User)

    @Query ("SELECT * FROM USER")
    suspend fun listUser() : List<User>
}