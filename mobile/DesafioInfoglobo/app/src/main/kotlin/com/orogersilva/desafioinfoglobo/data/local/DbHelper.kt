package com.orogersilva.desafioinfoglobo.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.orogersilva.desafioinfoglobo.data.local.contract.*

/**
 * Created by orogersilva on 3/24/2017.
 */
class DbHelper(context: Context, databaseName: String = "news.sqlite", databaseVersion: Int = 1)
    : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    // region PROPERTIES

    private val INTEGER_TYPE = " INTEGER"
    private val TEXT_TYPE = " TEXT"

    private val COMMA_SEPARATOR = ","

    // endregion

    // region SCRIPTS

    private val SQL_CREATE_NEWS_ENTRIES =
            "CREATE TABLE " + NewsPersistenceContract.TABLE_NAME + " (" +
                    NewsPersistenceContract.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT + INTEGER_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_UPDATED_IN + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_SECTION_NAME + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_SECTION_URL + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEPARATOR +
                    NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL + TEXT_TYPE +
                    ")"

    private val SQL_CREATE_AUTHOR_ENTRIES =
            "CREATE TABLE " + AuthorPersistenceContract.TABLE_NAME + " (" +
                    AuthorPersistenceContract.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEPARATOR +
                    AuthorPersistenceContract.COLUMN_NAME_NEWS_ID + INTEGER_TYPE + COMMA_SEPARATOR +
                    " FOREIGN KEY (" + AuthorPersistenceContract.COLUMN_NAME_NEWS_ID +
                    ") REFERENCES " + NewsPersistenceContract.TABLE_NAME + "(" + NewsPersistenceContract.COLUMN_NAME_ID + ")" +
                    ")"

    private val SQL_CREATE_VIDEO_ENTRIES =
            "CREATE TABLE " + VideoPersistenceContract.TABLE_NAME + " (" +
                    VideoPersistenceContract.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEPARATOR +
                    VideoPersistenceContract.COLUMN_NAME_NEWS_ID + INTEGER_TYPE + COMMA_SEPARATOR +
                    " FOREIGN KEY (" + VideoPersistenceContract.COLUMN_NAME_NEWS_ID +
                    ") REFERENCES " + NewsPersistenceContract.TABLE_NAME + "(" + NewsPersistenceContract.COLUMN_NAME_ID + ")" +
                    ")"

    private val SQL_CREATE_IMAGE_ENTRIES =
            "CREATE TABLE " + ImagePersistenceContract.TABLE_NAME + " (" +
                    ImagePersistenceContract.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEPARATOR +
                    ImagePersistenceContract.COLUMN_NAME_SOURCE + TEXT_TYPE + COMMA_SEPARATOR +
                    ImagePersistenceContract.COLUMN_NAME_CAPTION + TEXT_TYPE + COMMA_SEPARATOR +
                    ImagePersistenceContract.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEPARATOR +
                    ImagePersistenceContract.COLUMN_NAME_NEWS_ID + INTEGER_TYPE + COMMA_SEPARATOR +
                    " FOREIGN KEY (" + ImagePersistenceContract.COLUMN_NAME_NEWS_ID +
                    ") REFERENCES " + NewsPersistenceContract.TABLE_NAME + "(" + NewsPersistenceContract.COLUMN_NAME_ID + ")" +
                    ")"

    private val SQL_DELETE_IMAGE_ENTRIES =
        "DROP TABLE IF EXISTS " + ImagePersistenceContract.TABLE_NAME

    private val SQL_DELETE_VIDEO_ENTRIES =
        "DROP TABLE IF EXISTS " + VideoPersistenceContract.TABLE_NAME

    private val SQL_DELETE_AUTHOR_ENTRIES =
        "DROP TABLE IF EXISTS " + AuthorPersistenceContract.TABLE_NAME

    private val SQL_DELETE_NEWS_ENTRIES =
        "DROP TABLE IF EXISTS " + NewsPersistenceContract.TABLE_NAME

    // endregion

    // region OVERRIDED METHODS

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(SQL_CREATE_NEWS_ENTRIES)
        db?.execSQL(SQL_CREATE_AUTHOR_ENTRIES)
        db?.execSQL(SQL_CREATE_VIDEO_ENTRIES)
        db?.execSQL(SQL_CREATE_IMAGE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(SQL_DELETE_IMAGE_ENTRIES)
        db?.execSQL(SQL_DELETE_VIDEO_ENTRIES)
        db?.execSQL(SQL_DELETE_AUTHOR_ENTRIES)
        db?.execSQL(SQL_DELETE_NEWS_ENTRIES)

        onCreate(db)
    }

    // endregion
}