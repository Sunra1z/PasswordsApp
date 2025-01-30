package com.example.passwordsapp.feature_pass.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.passwordsapp.feature_pass.domain.model.Note

@Database(
    entities = [Note::class],
    version = 6 // Updated version
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE Note ADD COLUMN usernameIv BLOB NOT NULL DEFAULT X'00'
                """.trimIndent())
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
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

                database.execSQL("""
                    INSERT INTO Note_new (id, title, username, password, timeStamp, passwordIv, color)
                    SELECT id, title, 'placeholder_username', password, timeStamp, passwordIv, color
                    FROM Note
                """.trimIndent())

                database.execSQL("DROP TABLE IF EXISTS Note")
                database.execSQL("ALTER TABLE Note_new RENAME TO Note")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE Note ADD COLUMN newColumn TEXT DEFAULT 'default_value'
                """.trimIndent())
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE Note ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0
                """.trimIndent())
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE Note_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        username TEXT NOT NULL,
                        password BLOB NOT NULL,
                        timeStamp INTEGER NOT NULL,
                        passwordIv BLOB NOT NULL,
                        color INTEGER NOT NULL,
                        isFavorite INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())

                database.execSQL("""
                    INSERT INTO Note_new (id, title, username, password, timeStamp, passwordIv, color, isFavorite)
                    SELECT id, title, username, password, timeStamp, passwordIv, color, isFavorite
                    FROM Note
                """.trimIndent())

                database.execSQL("DROP TABLE Note")
                database.execSQL("ALTER TABLE Note_new RENAME TO Note")
            }
        }
    }
}