package com.example.passwordsapp.feature_pass.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.passwordsapp.feature_pass.domain.model.Note

@Database(
    entities = [Note::class],
    version = 3 // Incremented version
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 1 to version 2
                database.execSQL("""
                    ALTER TABLE Note ADD COLUMN usernameIv BLOB NOT NULL DEFAULT X'00'
                """.trimIndent())
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a new table with the updated schema
                database.execSQL("""
                    CREATE TABLE Note_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        username TEXT NOT NULL,
                        password BLOB NOT NULL,
                        timeStamp INTEGER NOT NULL,
                        passwordIv BLOB NOT NULL,
                        color INTEGER NOT NULL
                    )
                """.trimIndent())

                // Copy the data from the old table to the new table, replacing username with a placeholder value
                database.execSQL("""
                    INSERT INTO Note_new (id, title, username, password, timeStamp, passwordIv, color)
                    SELECT id, title, 'placeholder_username', password, timeStamp, passwordIv, color
                    FROM Note
                """.trimIndent())

                // Drop the old table
                database.execSQL("DROP TABLE IF EXISTS Note")

                // Rename the new table to the old table name
                database.execSQL("ALTER TABLE Note_new RENAME TO Note")
            }
        }
    }
}