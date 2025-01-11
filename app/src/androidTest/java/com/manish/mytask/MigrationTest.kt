package com.manish.mytask

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class MigrationTest {

//        private val TEST_DB = "migration_test_db"
    private lateinit var database: SupportSQLiteDatabase

    @Before
    fun setup() {
        // Create a database with version 3
        database = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            NoteDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
        ).createDatabase("test_db", 3)
    }

    @Test
    fun migrate3To4() {
        // Insert initial data for version 3
        database.execSQL("INSERT INTO notes (id, title) VALUES (1, 'Sample Note')")

        // Close the database at version 3
        database.close()

        // Run migration
        val migratedDb = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            NoteDatabase::class.java,
            "test_db"
        ).addMigrations(DatabaseProvider.migration3_4).build().openHelper.writableDatabase

        // Verify the schema changes
        val cursor = migratedDb.query("PRAGMA table_info(notes)")
        assertTrue(cursor.moveToFirst())

        // Check if the "description" column exists
        var hasDescription = false
        do {
            val columnName = cursor.getString(cursor.getColumnIndex("name"))
            if (columnName == "description") {
                hasDescription = true
                break
            }
        } while (cursor.moveToNext())

        assertTrue("Description column was not added", hasDescription)

        cursor.close()
        migratedDb.close()
    }
}