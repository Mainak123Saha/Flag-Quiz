package com.mainak.flagquiz.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mainak.flagquiz.model.FlagsModel

class FlagsDao {

    fun getRandomTenRecords(helper : DatabaseCopyHelper) : ArrayList<FlagsModel>{
        helper.openDataBase()
        val database = helper.myDataBase

        val cursor : Cursor = database.rawQuery("SELECT * FROM flags ORDER BY RANDOM() LIMIT 10",null)

        val idIndex : Int = cursor.getColumnIndex("flag_id")
        val countryNameIndex : Int = cursor.getColumnIndex("country_name")
        val flagNameIndex = cursor.getColumnIndex("flag_name")

        val recordList = ArrayList<FlagsModel>()

        while (cursor.moveToNext()){
            val record = FlagsModel(
                cursor.getInt(idIndex),
                cursor.getString(countryNameIndex),
                cursor.getString(flagNameIndex)
            )
            recordList.add(record)
        }

        cursor.close()
        helper.close()
        return recordList
    }

    fun getRandomThreeRecords(helper : DatabaseCopyHelper, id : Int) : ArrayList<FlagsModel>{
        helper.openDataBase()
        val recordList = ArrayList<FlagsModel>()
        val database = helper.myDataBase
        val cursor : Cursor = database.rawQuery("SELECT * FROM flags WHERE flag_id != $id ORDER BY RANDOM() LIMIT 3",null)
        // For selectionArgs we can also add where flag_id != ? and then in selectionArgs we can give arrayOf(The strings we want)


        val idIndex : Int = cursor.getColumnIndex("flag_id")
        val countryNameIndex : Int = cursor.getColumnIndex("country_name")
        val flagNameIndex = cursor.getColumnIndex("flag_name")

        while (cursor.moveToNext()){
            val record = FlagsModel(
                cursor.getInt(idIndex),
                cursor.getString(countryNameIndex),
                cursor.getString(flagNameIndex)
            )
            recordList.add(record)
        }

        cursor.close()
        helper.close()
        return recordList
    }

}