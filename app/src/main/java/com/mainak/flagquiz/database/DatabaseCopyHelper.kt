package com.mainak.flagquiz.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import kotlin.jvm.Throws

class DatabaseCopyHelper(context : Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    // Holds the database path
    var dbPath : File
    var myContext : Context
    lateinit var myDataBase : SQLiteDatabase

    companion object{
        var DB_NAME : String = "countries.db"
    }

    init{
        myContext = context
        dbPath = myContext.getDatabasePath(DB_NAME)
    }

    @Throws(IOException::class)
    private fun checkDataBase() : Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath.toString(),null,SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException){
            Log.d("data","The database don't exists so creating")
        }
        checkDB?.close()
        return checkDB != null
    }

    private fun copyDataBase() {
        val dbPath = myContext.getDatabasePath(DB_NAME)

        // Check if the database already exists
        if (dbPath.exists()) {
            Log.d("data","Don't know how the database now exists")
            // Database already exists, no need to copy
        }
        else {
            // Ensure the databases directory exists
            dbPath.parentFile?.mkdirs()
        }


        // Copy the database from assets to the databases directory
        val inputStream = myContext.assets.open(DB_NAME)
        val outputStream = FileOutputStream(dbPath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()

        Log.d("data", "Database size: ${dbPath.length()} bytes")

    }

    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist = checkDataBase()
        if(!dbExist){
            this.readableDatabase
            try {
                copyDataBase()
            } catch (e : IOException){
                Log.e("data","Error copying database: and it is " + e.message)
            }
        }
    }

    @Throws(SQLiteException::class)
    fun openDataBase() {
        myDataBase = SQLiteDatabase.openDatabase(dbPath.toString(),null, SQLiteDatabase.OPEN_READWRITE)
    }

    @Synchronized
    override fun close() {
        if (this::myDataBase.isInitialized && myDataBase.isOpen) {
            myDataBase.close()
        }
        super.close()
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS flags(flag_id INTEGER, country_name TEXT, flag_name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS flags")
        onCreate(db)
    }
}