package com.sertanozcnn.notesappadvanced.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sertanozcnn.notesappadvanced.entities.Notes

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM notes WHERE id = :id ")
    suspend fun getSpecificNote(id: Int): Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: Notes)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("DELETE FROM notes WHERE id = :id ")
    suspend fun deleteSpecificNote(id: Int)

    @Update
    suspend fun updateNote(notes: Notes)

}